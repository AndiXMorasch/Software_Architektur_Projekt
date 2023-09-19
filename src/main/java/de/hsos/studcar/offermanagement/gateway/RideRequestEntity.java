package de.hsos.studcar.offermanagement.gateway;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import de.hsos.studcar.offermanagement.entity.RideRequest;

@Entity
public class RideRequestEntity {

    @Id
    @SequenceGenerator(name = "rideRequestSeq", sequenceName = "rideRequest_id_seq")
    @GeneratedValue(generator = "rideRequestSeq")
    private Long id;

    @Version
    private Long version;

    private String passengerId;

    private String pickUpLocation;

    private String pickUpTime;

    private String destinationCampus;

    private boolean active;

    private boolean accepted;

    private String description;

    @ManyToOne
    @JoinColumn
    private OfferEntity offer;

    public static RideRequest getRideRequestFromEntity(RideRequestEntity entity) {
        return new RideRequest(entity.getId(), entity.getPassengerId(), entity.getPickUpLocation(),
                entity.getPickUpTime(),
                entity.getDestinationCampus(), entity.isActive(), entity.isAccepted(), entity.getDescription(),
                OfferEntity.getOfferFromEntity(entity.getOffer()));
    }

    public static Collection<RideRequest> getRequestsFromEntityList(Collection<RideRequestEntity> entities) {
        return entities.stream()
                .map(entity -> new RideRequest(entity.getId(), entity.getPassengerId(), entity.getPickUpLocation(),
                        entity.getPickUpTime(),
                        entity.getDestinationCampus(),
                        entity.isActive(),
                        entity.isAccepted(),
                        entity.getDescription(),
                        null))
                .toList();
    }

    public static RideRequestEntity getEntityFromRideRequest(RideRequest rideRequest) {
        RideRequestEntity entity = new RideRequestEntity();
        entity.setId(rideRequest.getId());
        entity.setPassengerId(rideRequest.getPassengerId());
        entity.setPickUpLocation(rideRequest.getPickUpLocation());
        entity.setPickUpTime(rideRequest.getPickUpTime());
        entity.setDestinationCampus(rideRequest.getDestinationCampus());
        entity.setActive(rideRequest.isActive());
        entity.setAccepted(rideRequest.isAccepted());
        entity.setDescription(rideRequest.getDescription());
        return entity;
    }

    public static Collection<RideRequestEntity> getEntitiesFromRideRequestList(Collection<RideRequest> rideRequests) {
        Collection<RideRequestEntity> entities = new ArrayList<>();
        rideRequests.stream().forEach(r -> entities.add(RideRequestEntity.getEntityFromRideRequest(r)));
        return entities;
    }

    public RideRequestEntity modifyRequest(RideRequest rideRequest) {
        this.setPickUpLocation(rideRequest.getPickUpLocation());
        this.setPickUpTime(rideRequest.getPickUpTime());
        this.setDestinationCampus(rideRequest.getDestinationCampus());
        this.setActive(rideRequest.isActive());
        this.setAccepted(rideRequest.isAccepted());
        this.setDescription(rideRequest.getDescription());
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getDestinationCampus() {
        return destinationCampus;
    }

    public void setDestinationCampus(String destinationCampus) {
        this.destinationCampus = destinationCampus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }
}
