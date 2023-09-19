package de.hsos.studcar.offermanagement.entity;

public class RideRequest {

    private Long id;

    private String passengerId;

    private String pickUpLocation;

    private String pickUpTime;

    private String destinationCampus;

    private boolean active;

    private boolean accepted;

    private String description;

    private Offer offer;

    public RideRequest() {
        this.accepted = false; // bei Neuanlage default false
        this.active = true; // bei Neuanlage default true
    }

    public RideRequest(Long id,
            String passengerId, String pickUpLocation, String pickUpTime, String destinationCampus,
            boolean active, boolean accepted, String description) {
        this.id = id;
        this.passengerId = passengerId;
        this.pickUpLocation = pickUpLocation;
        this.pickUpTime = pickUpTime;
        this.destinationCampus = destinationCampus;
        this.active = active;
        this.accepted = accepted;
        this.description = description;
    }

    public RideRequest(Long id,
            String passengerId, String pickUpLocation, String pickUpTime, String destinationCampus,
            boolean active, boolean accepted, String description, Offer offer) {
        this(id, passengerId, pickUpLocation, pickUpTime, destinationCampus, active, accepted, description);
        this.offer = offer;
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

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
