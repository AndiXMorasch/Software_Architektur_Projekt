package de.hsos.studcar.requestmanagement.control;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.DriveOfferCatalog;
import de.hsos.studcar.requestmanagement.entity.exceptions.AcceptInactiveOfferException;
import de.hsos.studcar.requestmanagement.entity.exceptions.DriveOfferAlreadyInactiveException;
import de.hsos.studcar.requestmanagement.entity.exceptions.RejectInactiveOfferException;
import de.hsos.studcar.shared.events.DriveOfferAcceptedEvent;
import de.hsos.studcar.shared.events.DriveOfferChangedEvent;
import de.hsos.studcar.shared.events.DriveOfferRejectedEvent;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class DriveOfferService implements ManageDriveOffers {

    @Inject
    Event<DriveOfferChangedEvent> driveOfferChangedEvent;

    @Inject
    Event<DriveOfferAcceptedEvent> driveOfferAcceptedEvent;

    @Inject
    Event<DriveOfferRejectedEvent> driveOfferRejectedEvent;

    @Inject
    DriveOfferCatalog offerCatalog;

    @Override
    public Collection<DriveOffer> selectAllDriveOffers(String driverId) {
        return this.offerCatalog.selectAllDriveOffers(driverId);
    }

    @Override
    public DriveOffer selectDriveOffer(String driverId, Long offerId) {
        return this.offerCatalog.selectDriveOffer(driverId, offerId);
    }

    @Override
    public boolean modifyDriveOffer(String driverId, DriveOffer driveOffer) {
        DriveOffer offer = this.offerCatalog.selectDriveOffer(driverId, driveOffer.getId());
        boolean modified = this.offerCatalog.modifyDriveOffer(driverId, driveOffer);

        if (modified) {
            fireDriveOfferChangedEvent(offer);
        }

        return modified;
    }

    @Override
    public boolean acceptDriveOffer(String passengerId, Long offerId) throws AcceptInactiveOfferException {
        boolean accepted = this.offerCatalog.acceptDriveOffer(passengerId, offerId);

        if (accepted) {
            DriveOffer offer = selectDriveOfferToOfferId(offerId);
            fireDriveOfferAcceptedEvent(offer);
        }

        return accepted;
    }

    @Override
    public boolean rejectDriveOffer(String passengerId, Long offerId) throws RejectInactiveOfferException {
        boolean rejected = this.offerCatalog.rejectDriveOffer(passengerId, offerId);

        if (rejected) {
            DriveOffer offer = selectDriveOfferToOfferId(offerId);
            fireDriveOfferRejectedEvent(offer);
        }

        return rejected;
    }

    @Override
    public boolean revokeDriveOffer(String driverId, Long offerId) throws DriveOfferAlreadyInactiveException {
        return this.offerCatalog.revokeDriveOffer(driverId, offerId);
    }

    private DriveOffer selectDriveOfferToOfferId(Long offerId) {
        return this.offerCatalog.selectDriveOfferToOfferId(offerId);
    }

    private void fireDriveOfferChangedEvent(DriveOffer driveOffer) {
        DriveOfferChangedEvent driveOfferChanged = new DriveOfferChangedEvent();
        driveOfferChanged.offerId = driveOffer.getId();
        driveOfferChanged.driverId = driveOffer.getDriverId();
        driveOfferChanged.requestId = driveOffer.getRequest().getId();
        driveOfferChanged.passengerId = driveOffer.getRequest().getPassengerId();
        driveOfferChangedEvent.fire(driveOfferChanged);
    }

    private void fireDriveOfferAcceptedEvent(DriveOffer driveOffer) {
        DriveOfferAcceptedEvent driveOfferAccepted = new DriveOfferAcceptedEvent();
        driveOfferAccepted.offerId = driveOffer.getId();
        driveOfferAccepted.driverId = driveOffer.getDriverId();
        driveOfferAccepted.requestId = driveOffer.getRequest().getId();
        driveOfferAccepted.passengerId = driveOffer.getRequest().getPassengerId();
        this.driveOfferAcceptedEvent.fire(driveOfferAccepted);
    }

    private void fireDriveOfferRejectedEvent(DriveOffer driveOffer) {
        DriveOfferRejectedEvent driveOfferRejected = new DriveOfferRejectedEvent();
        driveOfferRejected.offerId = driveOffer.getId();
        driveOfferRejected.driverId = driveOffer.getDriverId();
        driveOfferRejected.requestId = driveOffer.getRequest().getId();
        driveOfferRejected.passengerId = driveOffer.getRequest().getPassengerId();
        this.driveOfferRejectedEvent.fire(driveOfferRejected);
    }

}
