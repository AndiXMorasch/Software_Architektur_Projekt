package de.hsos.studcar.requestmanagement.entity;

import java.util.Collection;

import de.hsos.studcar.requestmanagement.entity.exceptions.AcceptInactiveOfferException;
import de.hsos.studcar.requestmanagement.entity.exceptions.DriveOfferAlreadyInactiveException;
import de.hsos.studcar.requestmanagement.entity.exceptions.RejectInactiveOfferException;

public interface DriveOfferCatalog {

    Collection<DriveOffer> selectAllDriveOffers(String driverId);

    DriveOffer selectDriveOffer(String driverId, Long offerId);

    boolean modifyDriveOffer(String driverId, DriveOffer driveOffer);

    boolean acceptDriveOffer(String passengerId, Long offerId) throws AcceptInactiveOfferException;

    boolean rejectDriveOffer(String passengerId, Long offerId) throws RejectInactiveOfferException;

    boolean revokeDriveOffer(String driverId, Long offerId) throws DriveOfferAlreadyInactiveException;

    DriveOffer selectDriveOfferToOfferId(Long offerId);
}
