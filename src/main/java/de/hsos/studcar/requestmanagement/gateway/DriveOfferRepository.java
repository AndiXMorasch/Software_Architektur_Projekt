package de.hsos.studcar.requestmanagement.gateway;

import javax.transaction.Transactional.TxType;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.DriveOfferCatalog;
import de.hsos.studcar.requestmanagement.entity.exceptions.AcceptInactiveOfferException;
import de.hsos.studcar.requestmanagement.entity.exceptions.DriveOfferAlreadyInactiveException;
import de.hsos.studcar.requestmanagement.entity.exceptions.RejectInactiveOfferException;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class DriveOfferRepository implements DriveOfferCatalog {

    @Inject
    EntityManager em;

    @Override
    public Collection<DriveOffer> selectAllDriveOffers(String driverId) {
        TypedQuery<DriveOfferEntity> findAll = em
                .createQuery("SELECT o FROM DriveOfferEntity o WHERE o.driverId =: driverId",
                        DriveOfferEntity.class);
        findAll.setParameter("driverId", driverId);
        List<DriveOfferEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(DriveOfferEntity::getDriveOfferFromEntity)
                .toList();
    }

    @Override
    public DriveOffer selectDriveOffer(String driverId, Long offerId) {
        DriveOfferEntity entity = em.find(DriveOfferEntity.class, offerId);

        if (entity == null) {
            return null;
        }

        return DriveOfferEntity.getDriveOfferFromEntity(entity);
    }

    @Override
    public DriveOffer selectDriveOfferToOfferId(Long offerId) {
        DriveOfferEntity entity = em.find(DriveOfferEntity.class, offerId);

        if (entity == null) {
            return null;
        }

        return DriveOfferEntity.getDriveOfferFromEntity(entity);
    }

    @Override
    public boolean modifyDriveOffer(String driverId, DriveOffer driveOffer) {
        DriveOfferEntity entity = em.find(DriveOfferEntity.class, driveOffer.getId());

        if (entity == null) {
            return false;
        }
        if (!entity.getDriverId().equals(driverId)) {
            throw new SecurityException("You are not allowed to modify this drive offer.");
        }

        entity.modifyOffer(driveOffer);
        em.merge(entity);
        return true;

    }

    @Override
    public boolean acceptDriveOffer(String passengerId, Long offerId) throws AcceptInactiveOfferException {
        DriveOfferEntity entity = em.find(DriveOfferEntity.class, offerId);

        if (entity == null) {
            return false;
        }
        if (!entity.getRequest().getPassengerId().equals(passengerId)) {
            throw new SecurityException("You are not allowed to accept for this request.");
        }

        if (entity.isActive()) {
            entity.setAccepted(true);
            this.em.merge(entity);
            return true;
        }

        throw new AcceptInactiveOfferException();
    }

    @Override
    public boolean rejectDriveOffer(String passengerId, Long offerId) throws RejectInactiveOfferException {
        DriveOfferEntity entity = em.find(DriveOfferEntity.class, offerId);

        if (entity == null) {
            return false;
        }
        if (!entity.getRequest().getPassengerId().equals(passengerId)) {
            throw new SecurityException("You are not allowed to reject for this request.");
        }

        if (entity.isActive()) {
            entity.setAccepted(false);
            entity.setActive(false);
            this.em.merge(entity);
            return true;
        }

        throw new RejectInactiveOfferException();
    }

    @Override
    public boolean revokeDriveOffer(String driverId, Long offerId) throws DriveOfferAlreadyInactiveException {
        DriveOfferEntity entity = em.find(DriveOfferEntity.class, offerId);

        if (entity == null) {
            return false;
        }
        if (!entity.getDriverId().equals(driverId)) {
            throw new SecurityException("You are not allowed to revoke for this drive offer.");
        }

        boolean oldStatus = entity.isActive();

        if (entity.isActive()) {
            entity.setActive(false);
        } else {
            throw new DriveOfferAlreadyInactiveException();
        }

        return oldStatus != entity.isActive();
    }

}
