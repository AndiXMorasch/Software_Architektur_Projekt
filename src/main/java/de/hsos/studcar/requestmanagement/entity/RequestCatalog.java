package de.hsos.studcar.requestmanagement.entity;

import java.util.Collection;

import de.hsos.studcar.requestmanagement.entity.exceptions.AddOfferToInactiveRequestException;

public interface RequestCatalog {

    Collection<Request> selectAllRequests();

    Collection<Request> selectAllRequestsByPassengerId(String passengerId);

    Request selectRequest(Long requestId);

    Long createRequest(Request request);

    boolean modifyRequest(String passengerId, Request request);

    boolean addDriveOfferToRequest(DriveOffer driveOffer, Long requestId) throws AddOfferToInactiveRequestException;

    boolean revokeRequest(String passengerId, Long requestId);
}
