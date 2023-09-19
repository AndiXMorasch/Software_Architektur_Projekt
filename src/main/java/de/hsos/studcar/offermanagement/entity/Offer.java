package de.hsos.studcar.offermanagement.entity;

import java.util.Collection;

public class Offer {

    private Long id;

    private String driverId;

    private String startLocation;

    private String destinationCampus;

    private String driveDate;

    private String arrivalTime;

    private Long freeSeats;

    private boolean active;

    private String description;

    private Collection<RideRequest> rideRequests;

    public Offer() {

    }

    public Offer(Long id, String driverId, String startLocation, String destinationCampus, String driveDate,
            String arrivalTime, Long freeSeats,
            boolean active, String description, Collection<RideRequest> rideRequests) {
        this.id = id;
        this.driverId = driverId;
        this.startLocation = startLocation;
        this.destinationCampus = destinationCampus;
        this.driveDate = driveDate;
        this.arrivalTime = arrivalTime;
        this.freeSeats = freeSeats;
        this.active = active;
        this.description = description;
        this.rideRequests = rideRequests;
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

    public Collection<RideRequest> getRideRequests() {
        return rideRequests;
    }

    public void setRideRequests(Collection<RideRequest> rideRequests) {
        this.rideRequests = rideRequests;
    }
}
