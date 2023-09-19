package de.hsos.studcar.offermanagement.boundary.api.dto;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.offermanagement.entity.RideRequest;

public class RideRequestDTO {

    @NotNull
    public Long id;

    public String passengerId;

    @NotNull
    @NotBlank
    public String pickUpLocation;

    @NotNull
    @NotBlank
    public String pickUpTime;

    @NotNull
    @NotBlank
    public String destinationCampus;

    @NotNull
    public boolean active;

    @NotNull
    public boolean accepted;

    public String description;

    public static RideRequestDTO mapRideRequestToDTO(RideRequest rideRequest) {
        RideRequestDTO dto = new RideRequestDTO();
        dto.id = rideRequest.getId();
        dto.passengerId = rideRequest.getPassengerId();
        dto.destinationCampus = rideRequest.getDestinationCampus();
        dto.pickUpLocation = rideRequest.getPickUpLocation();
        dto.pickUpTime = rideRequest.getPickUpTime();
        dto.accepted = rideRequest.isAccepted();
        dto.description = rideRequest.getDescription();
        dto.active = rideRequest.isActive();
        return dto;
    }

    public static Collection<RideRequestDTO> mapRequestsToDTOs(Collection<RideRequest> requests) {
        Collection<RideRequestDTO> dtos = new ArrayList<>();
        requests.stream().forEach(r -> dtos.add(mapRideRequestToDTO(r)));
        return dtos;
    }

    public static RideRequest mapDTOToRideRequest(RideRequestDTO dto) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setPassengerId(dto.passengerId);
        rideRequest.setDestinationCampus(dto.destinationCampus);
        rideRequest.setPickUpLocation(dto.pickUpLocation);
        rideRequest.setPickUpTime(dto.pickUpTime);
        rideRequest.setAccepted(dto.accepted);
        rideRequest.setDescription(dto.description);
        rideRequest.setActive(dto.active);
        return rideRequest;
    }

    public static Collection<RideRequest> mapDTOsToRideRequests(Collection<RideRequestDTO> dtos) {
        Collection<RideRequest> requests = new ArrayList<>();
        dtos.stream().forEach(r -> requests.add(mapDTOToRideRequest(r)));
        return requests;
    }

}
