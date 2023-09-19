package de.hsos.studcar.requestmanagement.entity;

public class DriveOffer {

    private Long id;

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

    private Request request;

    public DriveOffer() {
        this.active = true;
        this.accepted = false;
    }

    public DriveOffer(Long id, String driverId, String startLocation, String pickUpLocation, String destinationCampus,
            String arrivalTime, String pickUpTime, Long freeSeats,
            boolean active, boolean accepted, String description) {
        this.id = id;
        this.driverId = driverId;
        this.startLocation = startLocation;
        this.pickUpLocation = pickUpLocation;
        this.destinationCampus = destinationCampus;
        this.arrivalTime = arrivalTime;
        this.pickUpTime = pickUpTime;
        this.freeSeats = freeSeats;
        this.active = active;
        this.accepted = accepted;
        this.description = description;
    }

    public DriveOffer(Long id, String driverId, String startLocation, String pickUpLocation, String destinationCampus,
            String arrivalTime, String pickUpTime, Long freeSeats,
            boolean active, boolean accepted, String description, Request request) {
        this(id, driverId, startLocation, pickUpLocation, destinationCampus, arrivalTime, pickUpTime, freeSeats, active,
                accepted, description);
        this.request = request;
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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
