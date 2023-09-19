package de.hsos.studcar.requestmanagement.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.requestmanagement.entity.RequestCatalog;
import de.hsos.studcar.requestmanagement.entity.exceptions.AddOfferToInactiveRequestException;
import de.hsos.studcar.shared.events.NewDriveOfferCreatedEvent;
import de.hsos.studcar.shared.events.RequestChangedEvent;
import de.hsos.studcar.shared.events.RequestRevokedEvent;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class RequestService implements ManageRequests {

    @Inject
    Event<RequestChangedEvent> requestChangedEvent;

    @Inject
    Event<NewDriveOfferCreatedEvent> driveOfferCreatedEvent;

    @Inject
    Event<RequestRevokedEvent> requestRevokedEvent;

    @Inject
    RequestCatalog requestCatalog;

    @Override
    public Collection<Request> selectAllRequests(String sorting, String driveDate, String destinationCampus) {

        Collection<Request> requests = this.requestCatalog.selectAllRequests();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return requests.stream()
                .filter(d -> d.getDriveDate().compareTo(date) >= 0)
                .filter(r -> driveDate == null || driveDate.equals(r.getDriveDate()))
                .filter(o -> destinationCampus == null || destinationCampus.equals(o.getDestinationCampus()))
                .sorted((a, b) -> sortRequests(a, b, sorting))
                .collect(Collectors.toList());
    }

    private int sortRequests(Request a, Request b, String sorting) {
        if (sorting != null && sorting.equals("latest")) {
            return b.getDriveDate().compareTo(a.getDriveDate());
        }
        return a.getDriveDate().compareTo(b.getDriveDate());
    }

    @Override
    public Collection<Request> selectAllRequestsByPassengerId(String passengerId) {
        return this.requestCatalog.selectAllRequestsByPassengerId(passengerId);
    }

    @Override
    public Request selectRequest(Long requestId) {
        return this.requestCatalog.selectRequest(requestId);
    }

    @Override
    public boolean newOffersAvailable(String passengerId) {
        Collection<Request> requests = this.selectAllRequestsByPassengerId(passengerId);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return requests.stream()
                .filter(r -> r.getPassengerId().equals(passengerId))
                .filter(r -> r.getDriveDate().compareTo(date) >= 0)
                .filter(r -> r.isActive())
                .flatMap(r -> r.getDriveOffers().stream())
                .anyMatch(o -> o.isActive() && !o.isAccepted());
    }

    @Override
    public Long createRequest(String passengerId, Request request) {
        request.setPassengerId(passengerId);
        return this.requestCatalog.createRequest(request);
    }

    @Override
    public boolean modifyRequest(String passengerId, Request changedRequest) {
        boolean modified = this.requestCatalog.modifyRequest(passengerId, changedRequest);

        if (modified) {
            Request request = selectRequest(changedRequest.getId());
            fireRequestChangedEvent(request);
        }

        return modified;
    }

    @Override
    public boolean addDriveOfferToRequest(DriveOffer driveOffer, Long requestId)
            throws AddOfferToInactiveRequestException {
        boolean created = this.requestCatalog.addDriveOfferToRequest(driveOffer, requestId);

        if (created) {
            Request request = this.requestCatalog.selectRequest(requestId);
            fireDriveOfferCreatedEvent(driveOffer, request);
        }

        return created;
    }

    @Override
    public boolean revokeRequest(String passengerId, Long requestId) {
        Request revokedRequest = this.requestCatalog.selectRequest(requestId);
        boolean revoked = this.requestCatalog.revokeRequest(passengerId, requestId);

        if (revoked) {
            fireRequestRevokedEvent(revokedRequest);
        }

        return revoked;
    }

    private void fireDriveOfferCreatedEvent(DriveOffer driveOffer, Request request) {
        NewDriveOfferCreatedEvent driveOfferCreated = new NewDriveOfferCreatedEvent();
        driveOfferCreated.passengerId = request.getPassengerId();
        driveOfferCreated.driverId = driveOffer.getDriverId();
        driveOfferCreated.requestId = request.getId();
        driveOfferCreated.driveOfferId = driveOffer.getId();
        this.driveOfferCreatedEvent.fire(driveOfferCreated);
    }

    private void fireRequestChangedEvent(Request request) {
        RequestChangedEvent requestChanged = new RequestChangedEvent();
        requestChanged.requestId = request.getId();
        requestChanged.passengerId = request.getPassengerId();
        requestChanged.route = request.getPickUpLocation() + " to "
                + request.getDestinationCampus();
        requestChanged.date = request.getDriveDate();
        requestChanged.offerIdsToDriverIds = getOfferIdsAndDriverIds(request.getDriveOffers());
        this.requestChangedEvent.fire(requestChanged);
    }

    private void fireRequestRevokedEvent(Request request) {
        RequestRevokedEvent requestRevoked = new RequestRevokedEvent();
        requestRevoked.requestId = request.getId();
        requestRevoked.passengerId = request.getPassengerId();
        requestRevoked.route = request.getPickUpLocation() + " to "
                + request.getDestinationCampus();
        requestRevoked.date = request.getDriveDate();
        requestRevoked.offerIdsToDriverIds = getOfferIdsAndDriverIds(request.getDriveOffers());
        this.requestRevokedEvent.fire(requestRevoked);
    }

    private HashMap<Long, String> getOfferIdsAndDriverIds(Collection<DriveOffer> driveOffer) {
        HashMap<Long, String> offerIdsToDriverIds = new HashMap<>();
        for (DriveOffer d : driveOffer) {
            offerIdsToDriverIds.put(d.getId(), d.getDriverId());
        }
        return offerIdsToDriverIds;
    }

}
