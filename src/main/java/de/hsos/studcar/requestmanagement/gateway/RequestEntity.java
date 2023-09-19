package de.hsos.studcar.requestmanagement.gateway;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import de.hsos.studcar.requestmanagement.entity.Request;

@Entity
public class RequestEntity {

    @Id
    @SequenceGenerator(name = "requestSeq", sequenceName = "request_id_seq")
    @GeneratedValue(generator = "requestSeq")
    private Long id;

    @Version
    private Long version;

    private String passengerId;

    private String pickUpLocation;

    private String arrivalTime;

    private String destinationCampus;

    private String driveDate;

    private boolean active;

    private String description;

    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.REMOVE)
    private Collection<DriveOfferEntity> driveOffers;

    public static Request getRequestFromEntity(RequestEntity entity) {
        return new Request(entity.getId(), entity.getPassengerId(), entity.getPickUpLocation(),
                entity.getArrivalTime(), entity.getDestinationCampus(), entity.getDriveDate(), entity.isActive(),
                entity.getDescription(),
                DriveOfferEntity.getOffersFromEntityList(entity.getDriveOffers()));
    }

    public static RequestEntity getEntityFromRequest(Request request) {
        RequestEntity entity = new RequestEntity();

        entity.setPassengerId(request.getPassengerId());
        entity.setPickUpLocation(request.getPickUpLocation());
        entity.setArrivalTime(request.getArrivalTime());
        entity.setDestinationCampus(request.getDestinationCampus());
        entity.setDriveDate(request.getDriveDate());
        entity.setActive(request.isActive());
        entity.setDescription(request.getDescription());
        entity.setDriveOffers(new ArrayList<>()); // initial leere ArrayList
        return entity;
    }

    public RequestEntity modifyRequestEntity(Request request) {
        this.setPickUpLocation(request.getPickUpLocation());
        this.setArrivalTime(request.getArrivalTime());
        this.setDestinationCampus(request.getDestinationCampus());
        this.setDriveDate(request.getDriveDate());
        this.setActive(request.isActive());
        this.setDescription(request.getDescription());
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

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
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

    public Collection<DriveOfferEntity> getDriveOffers() {
        return driveOffers;
    }

    public void addDriveOfferToRequest(DriveOfferEntity driveOffer) {
        this.driveOffers.add(driveOffer);
        driveOffer.setRequest(this);
    }

    public void setDriveOffers(Collection<DriveOfferEntity> driveOffers) {
        this.driveOffers = driveOffers;
    }
}
