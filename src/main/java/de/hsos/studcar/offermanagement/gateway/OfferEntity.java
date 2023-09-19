package de.hsos.studcar.offermanagement.gateway;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import de.hsos.studcar.offermanagement.entity.Offer;

@Entity
public class OfferEntity {

    @Id
    @SequenceGenerator(name = "offerSeq", sequenceName = "offer_id_seq")
    @GeneratedValue(generator = "offerSeq")
    private Long id;

    @Version
    private Long version;

    private String driverId;

    private String startLocation;

    private String destinationCampus;

    private String driveDate;

    private String arrivalTime;

    private Long freeSeats;

    private boolean active;

    private String description;

    @OneToMany(mappedBy = "offer", fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.REMOVE)
    private Collection<RideRequestEntity> rideRequests;

    public static Offer getOfferFromEntity(OfferEntity entity) {
        return new Offer(entity.getId(), entity.getDriverId(), entity.getStartLocation(),
                entity.getDestinationCampus(),
                entity.getDriveDate(), entity.getArrivalTime(), entity.getFreeSeats(), entity.isActive(),
                entity.getDescription(),
                RideRequestEntity.getRequestsFromEntityList(entity.getRideRequests()));
    }

    public static OfferEntity getEntityFromOffer(Offer offer) {
        OfferEntity entity = new OfferEntity();

        entity.setDriverId(offer.getDriverId());
        entity.setStartLocation(offer.getStartLocation());
        entity.setDestinationCampus(offer.getDestinationCampus());
        entity.setDriveDate(offer.getDriveDate());
        entity.setArrivalTime(offer.getArrivalTime());
        entity.setFreeSeats(offer.getFreeSeats());
        entity.setActive(offer.isActive());
        entity.setDescription(offer.getDescription());
        entity.setRideRequests(new ArrayList<>()); // initial leere ArrayList
        return entity;
    }

    public OfferEntity modifyOfferEntity(Offer offer) {
        this.setStartLocation(offer.getStartLocation());
        this.setDestinationCampus(offer.getDestinationCampus());
        this.setDriveDate(offer.getDriveDate());
        this.setArrivalTime(offer.getArrivalTime());
        this.setFreeSeats(offer.getFreeSeats());
        this.setActive(offer.isActive());
        this.setDescription(offer.getDescription());
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestinationCampus() {
        return destinationCampus;
    }

    public void setDestinationCampus(String destinationCampus) {
        this.destinationCampus = destinationCampus;
    }

    public String getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(String driveDate) {
        this.driveDate = driveDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Long getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(Long freeSeats) {
        this.freeSeats = freeSeats;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<RideRequestEntity> getRideRequests() {
        return rideRequests;
    }

    public void addRideRequestToOffer(RideRequestEntity rideRequest) {
        this.rideRequests.add(rideRequest);
        rideRequest.setOffer(this);
    }

    public void replaceRideRequestData(RideRequestEntity rideRequest) {
        RideRequestEntity oldRequest = this.rideRequests.stream().filter(r -> r.getId() == rideRequest.getId())
                .findFirst()
                .get();
        oldRequest.setPassengerId(rideRequest.getPassengerId());
        oldRequest.setPickUpLocation(rideRequest.getPickUpLocation());
        oldRequest.setDestinationCampus(rideRequest.getDestinationCampus());
        oldRequest.setPickUpTime(rideRequest.getPickUpTime());
        oldRequest.setActive(rideRequest.isActive());
        oldRequest.setAccepted(rideRequest.isAccepted());
        oldRequest.setDescription(rideRequest.getDescription());
    }

    public boolean deleteRideRequest(Long requestId) {
        return this.rideRequests.removeIf(r -> r.getId() == requestId);
    }

    public void setRideRequests(Collection<RideRequestEntity> requests) {
        this.rideRequests = requests;
    }

}
