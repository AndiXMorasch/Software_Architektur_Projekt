package de.hsos.studcar.requestmanagement.gateway;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;

@Entity
public class DriveOfferEntity {

    @Id
    @SequenceGenerator(name = "driveOfferSeq", sequenceName = "driveOffer_id_seq")
    @GeneratedValue(generator = "driveOfferSeq")
    private Long id;

    @Version
    private Long version;

    private String driverId;

    private String startLocation;

    private String pickUpLocation;

    private String destinationCampus;

    private String arrivalTime;

    private String pickUpTime;

    private Long freeSeats;

    private boolean active;

    private boolean accepted;

    private String description;

    @ManyToOne
    @JoinColumn
    private RequestEntity request;

    public static DriveOffer getDriveOfferFromEntity(DriveOfferEntity entity) {
        return new DriveOffer(entity.getId(), entity.getDriverId(), entity.getStartLocation(),
                entity.getPickUpLocation(),
                entity.getDestinationCampus(), entity.getArrivalTime(), entity.getPickUpTime(), entity.getFreeSeats(),
                entity.isActive(),
                entity.isAccepted(), entity.getDescription(), RequestEntity.getRequestFromEntity(entity.getRequest()));
    }

    public DriveOfferEntity modifyOffer(DriveOffer driveOffer) {
        this.setStartLocation(driveOffer.getStartLocation());
        this.setPickUpLocation(driveOffer.getPickUpLocation());
        this.setDestinationCampus(driveOffer.getDestinationCampus());
        this.setArrivalTime(driveOffer.getArrivalTime());
        this.setPickUpTime(driveOffer.getPickUpTime());
        this.setFreeSeats(driveOffer.getFreeSeats());
        this.setActive(driveOffer.isActive());
        this.setAccepted(driveOffer.isAccepted());
        this.setDescription(driveOffer.getDescription());
        return this;
    }

    public static Collection<DriveOffer> getOffersFromEntityList(Collection<DriveOfferEntity> entities) {

        return entities.stream().map(entity -> new DriveOffer(entity.getId(), entity.getDriverId(),
                entity.getStartLocation(),
                entity.getPickUpLocation(),
                entity.getDestinationCampus(), entity.getArrivalTime(), entity.getPickUpTime(), entity.getFreeSeats(),
                entity.isActive(),
                entity.isAccepted(), entity.getDescription(),
                null))
                .toList();

    }

    public static DriveOfferEntity getEntityFromDriveOffer(DriveOffer driveOffer) {
        DriveOfferEntity entity = new DriveOfferEntity();
        entity.setId(driveOffer.getId());
        entity.setDriverId(driveOffer.getDriverId());
        entity.setStartLocation(driveOffer.getStartLocation());
        entity.setPickUpLocation(driveOffer.getPickUpLocation());
        entity.setDestinationCampus(driveOffer.getDestinationCampus());
        entity.setArrivalTime(driveOffer.getArrivalTime());
        entity.setPickUpTime(driveOffer.getPickUpTime());
        entity.setFreeSeats(driveOffer.getFreeSeats());
        entity.setActive(driveOffer.isActive());
        entity.setAccepted(driveOffer.isAccepted());
        entity.setDescription(driveOffer.getDescription());
        return entity;
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

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getDestinationCampus() {
        return destinationCampus;
    }

    public void setDestinationCampus(String destinationCampus) {
        this.destinationCampus = destinationCampus;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
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

    public RequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestEntity request) {
        this.request = request;
    }
}
