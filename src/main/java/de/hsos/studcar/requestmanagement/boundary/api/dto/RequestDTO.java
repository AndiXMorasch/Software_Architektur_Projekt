package de.hsos.studcar.requestmanagement.boundary.api.dto;

import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.requestmanagement.entity.Request;

public class RequestDTO {

    @NotNull
    public Long id;

    public String passengerId;

    @NotNull
    @NotBlank
    public String pickUpLocation;

    @NotNull
    @NotBlank
    public String arrivalTime;

    @NotNull
    @NotBlank
    public String destinationCampus;

    @NotNull
    @NotBlank
    public String driveDate;

    @NotNull
    public boolean active;

    public String description;

    public Collection<DriveOfferDTO> driveOffers;

    public static RequestDTO mapRequestToDTO(Request request) {
        RequestDTO dto = new RequestDTO();
        dto.id = request.getId();
        dto.passengerId = request.getPassengerId();
        dto.pickUpLocation = request.getPickUpLocation();
        dto.arrivalTime = request.getArrivalTime();
        dto.destinationCampus = request.getDestinationCampus();
        dto.driveDate = request.getDriveDate();
        dto.active = request.isActive();
        dto.description = request.getDescription();
        dto.driveOffers = DriveOfferDTO.mapDriveOffersToDTOs(request.getDriveOffers());
        return dto;
    }

    public static Request mapDTOToRequest(RequestDTO dto) {
        Request request = new Request();
        request.setId(dto.id);
        request.setPassengerId(dto.passengerId);
        request.setPickUpLocation(dto.pickUpLocation);
        request.setArrivalTime(dto.arrivalTime);
        request.setDestinationCampus(dto.destinationCampus);
        request.setDriveDate(dto.driveDate);
        request.setActive(dto.active);
        request.setDescription(dto.description);
        return request;
    }
}
