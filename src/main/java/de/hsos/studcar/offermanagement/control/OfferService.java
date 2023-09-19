package de.hsos.studcar.offermanagement.control;

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

import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.offermanagement.entity.OfferCatalog;
import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.exceptions.AddRequestToInactiveOfferException;
import de.hsos.studcar.shared.events.NewRideRequestCreatedEvent;
import de.hsos.studcar.shared.events.OfferChangedEvent;
import de.hsos.studcar.shared.events.OfferRevokedEvent;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class OfferService implements ManageOffers {

    @Inject
    Event<OfferChangedEvent> offerChangedEvent;

    @Inject
    Event<NewRideRequestCreatedEvent> newRequestCreatedEvent;

    @Inject
    Event<OfferRevokedEvent> offerRevokedEvent;

    @Inject
    OfferCatalog offerCatalog;

    @Override
    public Collection<Offer> selectAllOffers(String sorting, String driveDate, String destinationCampus,
            Integer freeSeats) {

        Collection<Offer> offers = this.offerCatalog.selectAllOffers();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return offers.stream()
                .filter(o -> o.getDriveDate().compareTo(date) >= 0)
                .filter(o -> driveDate == null || driveDate.equals(o.getDriveDate()))
                .filter(o -> destinationCampus == null || destinationCampus.equals(o.getDestinationCampus()))
                .filter(o -> freeSeats == null || freeSeats <= o.getFreeSeats())
                .sorted((a, b) -> sortOffers(a, b, sorting))
                .collect(Collectors.toList());
    }

    private int sortOffers(Offer a, Offer b, String sorting) {
        if (sorting != null && sorting.equals("latest")) {
            return b.getDriveDate().compareTo(a.getDriveDate());
        }

        return a.getDriveDate().compareTo(b.getDriveDate());
    }

    @Override
    public Collection<Offer> selectAllOffersByDriverId(String driverId) {
        Collection<Offer> offers = this.offerCatalog.selectAllOffersByDriverId(driverId);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return offers.stream()
                .filter(o -> o.getDriveDate().compareTo(date) >= 0)
                .sorted((a, b) -> sortOffers(a, b, null))
                .collect(Collectors.toList());
    }

    @Override
    public boolean newRequestsAvailable(String driverId) {
        Collection<Offer> offers = this.offerCatalog.selectAllOffers();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return offers.stream()
                .filter(o -> o.getDriverId().equals(driverId))
                .filter(o -> o.isActive())
                .filter(o -> o.getDriveDate().compareTo(date) >= 0)
                .filter(o -> o.getFreeSeats() > 0)
                .flatMap(o -> o.getRideRequests().stream())
                .anyMatch(r -> r.isActive() && !r.isAccepted());
    }

    @Override
    public Offer selectOffer(Long offerId) {
        return this.offerCatalog.selectOffer(offerId);
    }

    @Override
    public Long createOffer(String driverId, Offer offer) {
        offer.setDriverId(driverId);
        return this.offerCatalog.createOffer(offer);
    }

    @Override
    public boolean modifyOffer(String driverId, Offer changedOffer) {
        boolean modified = this.offerCatalog.modifyOffer(driverId, changedOffer);

        if (modified) {
            Offer offer = selectOffer(changedOffer.getId());
            fireOfferChangedEvent(offer);
        }

        return modified;
    }

    @Override
    public boolean addRideRequestToOffer(RideRequest rideRequest, Long offerId)
            throws AddRequestToInactiveOfferException {

        boolean created = this.offerCatalog.addRideRequestToOffer(rideRequest, offerId);

        if (created) {
            Offer offer = this.offerCatalog.selectOffer(offerId);
            fireRideRequestCreatedEvent(rideRequest, offer);
        }

        return created;
    }

    @Override
    public boolean revokeOffer(String driverId, Long offerId) {
        Offer revokedOffer = this.offerCatalog.selectOffer(offerId);
        boolean revoked = this.offerCatalog.revokeOffer(driverId, offerId);

        if (revoked) {
            fireOfferRevokedEvent(revokedOffer);
        }

        return revoked;
    }

    private void fireRideRequestCreatedEvent(RideRequest rideRequest, Offer offer) {
        NewRideRequestCreatedEvent rideRequestCreated = new NewRideRequestCreatedEvent();
        rideRequestCreated.driverId = offer.getDriverId();
        rideRequestCreated.offerId = offer.getId();
        rideRequestCreated.passengerId = rideRequest.getPassengerId();
        rideRequestCreated.rideRequestId = rideRequest.getId();
        this.newRequestCreatedEvent.fire(rideRequestCreated);
    }

    private void fireOfferChangedEvent(Offer offer) {
        OfferChangedEvent offerChanged = new OfferChangedEvent();
        offerChanged.offerId = offer.getId();
        offerChanged.driverId = offer.getDriverId();
        offerChanged.route = offer.getStartLocation() + " to " + offer.getDestinationCampus();
        offerChanged.date = offer.getDriveDate();
        offerChanged.requestIdsToPassengerIds = getRequestIdsAndPassengerIds(offer.getRideRequests());
        this.offerChangedEvent.fire(offerChanged);
    }

    private void fireOfferRevokedEvent(Offer offer) {
        OfferRevokedEvent offerRevoked = new OfferRevokedEvent();
        offerRevoked.driverId = offer.getDriverId();
        offerRevoked.offerId = offer.getId();
        offerRevoked.route = offer.getStartLocation() + " to " + offer.getDestinationCampus();
        offerRevoked.date = offer.getDriveDate();
        offerRevoked.requestIdsToPassengerIds = getRequestIdsAndPassengerIds(offer.getRideRequests());
        this.offerRevokedEvent.fire(offerRevoked);
    }

    private HashMap<Long, String> getRequestIdsAndPassengerIds(Collection<RideRequest> rideRequests) {
        HashMap<Long, String> requestIdsToPassengerIds = new HashMap<>();
        for (RideRequest r : rideRequests) {
            requestIdsToPassengerIds.put(r.getId(), r.getPassengerId());
        }
        return requestIdsToPassengerIds;
    }

}
