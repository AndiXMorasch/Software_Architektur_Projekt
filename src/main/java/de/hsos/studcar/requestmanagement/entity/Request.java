package de.hsos.studcar.requestmanagement.entity;

import java.util.Collection;

public class Request {

    private Long id;

    private String passengerId;

    private String pickUpLocation;

    private String arrivalTime;

    private String destinationCampus;

    private String driveDate;

    private boolean active;

    private String description;

    private Collection<DriveOffer> driveOffers;

    public Request() {

    }

    public Request(Long id, String passengerId, String pickUpLocation, String arrivalTime,
            String destinationCampus, String driveDate, boolean active, String description,
            Collection<DriveOffer> driveOffers) {
        this.id = id;
        this.passengerId = passengerId;
        this.pickUpLocation = pickUpLocation;
        this.arrivalTime = arrivalTime;
        this.destinationCampus = destinationCampus;
        this.driveDate = driveDate;
        this.active = active;
        this.description = description;
        this.driveOffers = driveOffers;
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

    public Collection<DriveOffer> getDriveOffers() {
        return driveOffers;
    }

    public void setDriveOffers(Collection<DriveOffer> driveOffers) {
        this.driveOffers = driveOffers;
    }
}
