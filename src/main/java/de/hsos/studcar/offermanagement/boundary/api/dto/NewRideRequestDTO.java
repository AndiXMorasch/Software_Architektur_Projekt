package de.hsos.studcar.offermanagement.boundary.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.offermanagement.entity.RideRequest;

public class NewRideRequestDTO {

    @NotNull
    @NotBlank
    public String pickUpLocation;

    @NotNull
    @NotBlank
    public String pickUpTime;

    @NotNull
    @NotBlank
    public String destinationCampus;

    public String description;

    public static RideRequest mapDTOToRideRequest(NewRideRequestDTO dto) {
        RideRequest rideRequest = new RideRequest();

        rideRequest.setPickUpLocation(dto.pickUpLocation);
        rideRequest.setPickUpTime(dto.pickUpTime);
        rideRequest.setDestinationCampus(dto.destinationCampus);
        rideRequest.setDescription(dto.description);

        return rideRequest;
    }
}
