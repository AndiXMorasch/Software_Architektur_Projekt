package de.hsos.studcar.offermanagement.gateway;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.offermanagement.entity.OfferCatalog;
import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.exceptions.AddRequestToInactiveOfferException;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class OfferRepository implements OfferCatalog {

    @Inject
    EntityManager em;

    @Override
    public Collection<Offer> selectAllOffers() {
        TypedQuery<OfferEntity> findAll = em.createQuery("SELECT o FROM OfferEntity o",
                OfferEntity.class);
        List<OfferEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(OfferEntity::getOfferFromEntity)
                .toList();
    }

    @Override
    public Collection<Offer> selectAllOffersByDriverId(String driverId) {
        TypedQuery<OfferEntity> findAll = em.createQuery(
                "SELECT o FROM OfferEntity o WHERE o.driverId =: driverId ",
                OfferEntity.class);
        findAll.setParameter("driverId", driverId);
        List<OfferEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(OfferEntity::getOfferFromEntity)
                .toList();
    }

    @Override
    public Offer selectOffer(Long offerId) {
        OfferEntity entity = em.find(OfferEntity.class, offerId);

        if (entity == null) {
            return null;
        }

        return OfferEntity.getOfferFromEntity(entity);
    }

    @Override
    public Long createOffer(Offer offer) {
        OfferEntity entity = OfferEntity.getEntityFromOffer(offer);

        this.em.persist(entity);
        return entity.getId();
    }

    @Override
    public boolean modifyOffer(String driverId, Offer offer) {
        OfferEntity entity = em.find(OfferEntity.class, offer.getId());

        if (entity == null) {
            return false;
        }
        if (!entity.getDriverId().equals(driverId)) {
            throw new SecurityException("You are not allowed to modify this offer!");
        }

        entity.modifyOfferEntity(offer);
        em.merge(entity);
        return true;
    }

    @Override
    public boolean addRideRequestToOffer(RideRequest rideRequest, Long offerId)
            throws AddRequestToInactiveOfferException {
        OfferEntity entity = em.find(OfferEntity.class, offerId);

        if (entity == null) {
            return false;
        }
        if (!entity.isActive()) {
            throw new AddRequestToInactiveOfferException();
        }

        int requestCount = entity.getRideRequests().size();

        RideRequestEntity rideRequestEntity = RideRequestEntity.getEntityFromRideRequest(rideRequest);
        this.em.persist(rideRequestEntity);
        entity.addRideRequestToOffer(rideRequestEntity);

        this.em.merge(entity);
        return requestCount < entity.getRideRequests().size();
    }

    @Override
    public boolean revokeOffer(String driverId, Long offerId) {
        OfferEntity entity = em.find(OfferEntity.class, offerId);

        if (entity == null) {
            return false;
        }
        if (!entity.getDriverId().equals(driverId)) {
            throw new SecurityException("You are not allowed to revoke this offer!");
        }

        em.remove(entity);
        return true;
    }
}
