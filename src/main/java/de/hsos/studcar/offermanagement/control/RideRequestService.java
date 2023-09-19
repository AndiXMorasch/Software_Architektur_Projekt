package de.hsos.studcar.offermanagement.control;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.RideRequestCatalog;
import de.hsos.studcar.offermanagement.entity.exceptions.AcceptInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.NoMoreSeatsAvailableException;
import de.hsos.studcar.offermanagement.entity.exceptions.RejectInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.RideRequestAlreadyInactiveException;
import de.hsos.studcar.shared.events.RideRequestAcceptedEvent;
import de.hsos.studcar.shared.events.RideRequestChangedEvent;
import de.hsos.studcar.shared.events.RideRequestRejectedEvent;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class RideRequestService implements ManageRideRequests {

    @Inject
    Event<RideRequestChangedEvent> rideRequestChangedEvent;

    @Inject
    Event<RideRequestAcceptedEvent> rideRequestAcceptedEvent;

    @Inject
    Event<RideRequestRejectedEvent> rideRequestRejectedEvent;

    @Inject
    RideRequestCatalog requestCatalog;

    @Override
    public Collection<RideRequest> selectAllRideRequests(String passengerId) {
        return this.requestCatalog.selectAllRideRequests(passengerId);
    }

    @Override
    public RideRequest selectRideRequest(String passengerId, Long requestId) {
        return this.requestCatalog.selectRideRequest(passengerId, requestId);
    }

    @Override
    public boolean modifyRideRequest(String passengerId, RideRequest rideRequest) {
        RideRequest request = this.requestCatalog.selectRideRequest(passengerId, rideRequest.getId());
        boolean modified = this.requestCatalog.modifyRideRequest(passengerId, rideRequest);

        if (modified) {
            fireRideRequestChangedEvent(request);
        }

        return modified;
    }

    @Override
    public boolean acceptRideRequest(String driverId, Long requestId)
            throws AcceptInactiveRequestException, NoMoreSeatsAvailableException {
        boolean accepted = this.requestCatalog.acceptRideRequest(driverId, requestId);

        if (accepted) {
            RideRequest rideRequest = selectRideRequestToRequestId(requestId);
            fireRideRequestAcceptedEvent(rideRequest);
        }

        return accepted;
    }

    @Override
    public boolean rejectRideRequest(String driverId, Long requestId) throws RejectInactiveRequestException {
        boolean rejected = this.requestCatalog.rejectRideRequest(driverId, requestId);

        if (rejected) {
            RideRequest rideRequest = selectRideRequestToRequestId(requestId);
            fireRideRequestRejectedEvent(rideRequest);
        }

        return rejected;
    }

    @Override
    public boolean revokeRideRequest(String passengerId, Long requestId) throws RideRequestAlreadyInactiveException {
        return this.requestCatalog.revokeRideRequest(passengerId, requestId);
    }

    private RideRequest selectRideRequestToRequestId(Long requestId) {
        return this.requestCatalog.selectRideRequestToRequestId(requestId);
    }

    private void fireRideRequestChangedEvent(RideRequest rideRequest) {
        RideRequestChangedEvent rideRequestChanged = new RideRequestChangedEvent();
        rideRequestChanged.offerId = rideRequest.getOffer().getId();
        rideRequestChanged.driverId = rideRequest.getOffer().getDriverId();
        rideRequestChanged.requestId = rideRequest.getId();
        rideRequestChanged.passengerId = rideRequest.getPassengerId();
        this.rideRequestChangedEvent.fire(rideRequestChanged);
    }

    private void fireRideRequestAcceptedEvent(RideRequest rideRequest) {
        RideRequestAcceptedEvent rideRequestAccepted = new RideRequestAcceptedEvent();
        rideRequestAccepted.offerId = rideRequest.getOffer().getId();
        rideRequestAccepted.driverId = rideRequest.getOffer().getDriverId();
        rideRequestAccepted.requestId = rideRequest.getId();
        rideRequestAccepted.passengerId = rideRequest.getPassengerId();
        this.rideRequestAcceptedEvent.fire(rideRequestAccepted);
    }

    private void fireRideRequestRejectedEvent(RideRequest rideRequest) {
        RideRequestRejectedEvent rideRequestRejected = new RideRequestRejectedEvent();
        rideRequestRejected.offerId = rideRequest.getOffer().getId();
        rideRequestRejected.driverId = rideRequest.getOffer().getDriverId();
        rideRequestRejected.requestId = rideRequest.getId();
        rideRequestRejected.passengerId = rideRequest.getPassengerId();
        this.rideRequestRejectedEvent.fire(rideRequestRejected);
    }

}
