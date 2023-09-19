package de.hsos.studcar.offermanagement.gateway;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.RideRequestCatalog;
import de.hsos.studcar.offermanagement.entity.exceptions.AcceptInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.NoMoreSeatsAvailableException;
import de.hsos.studcar.offermanagement.entity.exceptions.RejectInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.RideRequestAlreadyInactiveException;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class RideRequestRepository implements RideRequestCatalog {

    @Inject
    EntityManager em;

    @Override
    public Collection<RideRequest> selectAllRideRequests(String passengerId) {
        TypedQuery<RideRequestEntity> findAll = em
                .createQuery("SELECT r FROM RideRequestEntity r WHERE r.passengerId =: passengerId",
                        RideRequestEntity.class);
        findAll.setParameter("passengerId", passengerId);
        List<RideRequestEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(RideRequestEntity::getRideRequestFromEntity)
                .toList();
    }

    @Override
    public RideRequest selectRideRequest(String passengerId, Long requestId) {
        RideRequestEntity entity = em.find(RideRequestEntity.class, requestId);

        if (entity == null) {
            return null;
        }

        return RideRequestEntity.getRideRequestFromEntity(entity);
    }

    @Override
    public RideRequest selectRideRequestToRequestId(Long requestId) {
        RideRequestEntity entity = em.find(RideRequestEntity.class, requestId);

        if (entity == null) {
            return null;
        }

        return RideRequestEntity.getRideRequestFromEntity(entity);
    }

    @Override
    public boolean modifyRideRequest(String passengerId, RideRequest rideRequest) {
        RideRequestEntity entity = em.find(RideRequestEntity.class, rideRequest.getId());

        if (entity == null) {
            return false;
        }
        if (!entity.getPassengerId().equals(passengerId)) {
            throw new SecurityException("You are not allowed to modify this ride request!");
        }

        entity.modifyRequest(rideRequest);
        em.merge(entity);
        return true;

    }

    @Override
    public boolean acceptRideRequest(String driverId, Long requestId)
            throws AcceptInactiveRequestException, NoMoreSeatsAvailableException {
        RideRequestEntity entity = em.find(RideRequestEntity.class, requestId);

        if (entity == null) {
            return false;
        }
        if (entity.getOffer().getFreeSeats()
                - entity.getOffer().getRideRequests().stream().filter(r -> r.isAccepted()).count() < 1) {
            throw new NoMoreSeatsAvailableException();
        }
        if (!entity.getOffer().getDriverId().equals(driverId)) {
            throw new SecurityException("You are not allowed to accept for this offer!");
        }

        if (entity.isActive()) {
            entity.setAccepted(true);
            this.em.merge(entity);
            return true;
        }

        throw new AcceptInactiveRequestException();
    }

    @Override
    public boolean rejectRideRequest(String driverId, Long requestId) throws RejectInactiveRequestException {
        RideRequestEntity entity = em.find(RideRequestEntity.class, requestId);

        if (entity == null) {
            return false;
        }

        if (!entity.getOffer().getDriverId().equals(driverId)) {
            throw new SecurityException("You are not allowed to reject for this offer!");
        }

        if (entity.isActive()) {
            entity.setAccepted(false);
            entity.setActive(false);
            this.em.merge(entity);
            return true;
        }

        throw new RejectInactiveRequestException();
    }

    @Override
    public boolean revokeRideRequest(String passengerId, Long requestId) throws RideRequestAlreadyInactiveException {
        RideRequestEntity entity = em.find(RideRequestEntity.class, requestId);

        if (entity == null) {
            return false;
        }

        if (!entity.getPassengerId().equals(passengerId)) {
            throw new SecurityException("You are not allowed to revoke for this ride request!");
        }

        boolean oldStatus = entity.isActive();

        if (entity.isActive()) {
            entity.setActive(false);
            em.merge(entity);
        } else {
            throw new RideRequestAlreadyInactiveException();
        }

        return oldStatus != entity.isActive();
    }

}
