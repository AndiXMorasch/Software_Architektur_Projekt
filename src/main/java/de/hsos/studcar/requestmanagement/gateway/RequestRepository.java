package de.hsos.studcar.requestmanagement.gateway;

import javax.transaction.Transactional.TxType;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.requestmanagement.entity.RequestCatalog;
import de.hsos.studcar.requestmanagement.entity.exceptions.AddOfferToInactiveRequestException;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class RequestRepository implements RequestCatalog {

    @Inject
    EntityManager em;

    @Override
    public Collection<Request> selectAllRequests() {
        TypedQuery<RequestEntity> findAll = em.createQuery("SELECT r FROM RequestEntity r",
                RequestEntity.class);
        List<RequestEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(RequestEntity::getRequestFromEntity)
                .toList();
    }

    @Override
    public Collection<Request> selectAllRequestsByPassengerId(String passengerId) {
        TypedQuery<RequestEntity> findAll = em.createQuery(
                "SELECT r FROM RequestEntity r WHERE r.passengerId =: passengerId ",
                RequestEntity.class);
        findAll.setParameter("passengerId", passengerId);
        List<RequestEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(RequestEntity::getRequestFromEntity)
                .toList();
    }

    @Override
    public Request selectRequest(Long requestId) {
        RequestEntity entity = em.find(RequestEntity.class, requestId);

        if (entity == null) {
            return null;
        }

        return RequestEntity.getRequestFromEntity(entity);
    }

    @Override
    public Long createRequest(Request request) {
        RequestEntity entity = RequestEntity.getEntityFromRequest(request);

        this.em.persist(entity);
        return entity.getId();
    }

    @Override
    public boolean modifyRequest(String passengerId, Request request) {
        RequestEntity entity = em.find(RequestEntity.class, request.getId());
        if (entity == null) {
            return false;
        }

        if (!entity.getPassengerId().equals(passengerId)) {
            throw new SecurityException("You are not allowed to modify this request.");
        }
        entity.modifyRequestEntity(request);
        em.merge(entity);
        return true;
    }

    @Override
    public boolean addDriveOfferToRequest(DriveOffer driveOffer, Long requestId)
            throws AddOfferToInactiveRequestException {
        RequestEntity entity = em.find(RequestEntity.class, requestId);

        if (entity == null) {
            return false;
        }
        if (!entity.isActive()) {
            throw new AddOfferToInactiveRequestException();
        }

        int requestCount = entity.getDriveOffers().size();

        DriveOfferEntity driveOfferEntity = DriveOfferEntity.getEntityFromDriveOffer(driveOffer);
        this.em.persist(driveOfferEntity);
        entity.addDriveOfferToRequest(driveOfferEntity);

        this.em.merge(entity);
        return requestCount < entity.getDriveOffers().size();
    }

    @Override
    public boolean revokeRequest(String passengerId, Long requestId) {
        RequestEntity entity = em.find(RequestEntity.class, requestId);

        if (entity == null) {
            return false;
        }
        if (!entity.getPassengerId().equals(passengerId)) {
            throw new SecurityException("You are not allowed to revoke this request.");
        }

        em.remove(entity);
        return true;
    }
}
