package de.hsos.studcar.requestmanagement.control;

import java.util.Collection;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.requestmanagement.entity.exceptions.AddOfferToInactiveRequestException;

public interface ManageRequests {

    Collection<Request> selectAllRequests(String sorting, String driveDate, String destinationCampus);

    Collection<Request> selectAllRequestsByPassengerId(String passengerId);

    Request selectRequest(Long requestId);

    boolean newOffersAvailable(String passengerId);

    Long createRequest(String passengerId, Request request);

    boolean modifyRequest(String passengerId, Request request);

    boolean addDriveOfferToRequest(DriveOffer driveOffer, Long requestId) throws AddOfferToInactiveRequestException;

    boolean revokeRequest(String passengerId, Long requestId);
}
