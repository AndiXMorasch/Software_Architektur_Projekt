package de.hsos.studcar.offermanagement.entity;

import java.util.Collection;

import de.hsos.studcar.offermanagement.entity.exceptions.AcceptInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.NoMoreSeatsAvailableException;
import de.hsos.studcar.offermanagement.entity.exceptions.RejectInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.RideRequestAlreadyInactiveException;

public interface RideRequestCatalog {

    Collection<RideRequest> selectAllRideRequests(String passengerId);

    RideRequest selectRideRequest(String passengerId, Long requestId);

    boolean modifyRideRequest(String passengerId, RideRequest rideRequest);

    boolean acceptRideRequest(String driverId, Long requestId)
            throws AcceptInactiveRequestException, NoMoreSeatsAvailableException;

    boolean rejectRideRequest(String driverId, Long requestId) throws RejectInactiveRequestException;

    boolean revokeRideRequest(String passengerId, Long requestId) throws RideRequestAlreadyInactiveException;

    RideRequest selectRideRequestToRequestId(Long requestId);
}
