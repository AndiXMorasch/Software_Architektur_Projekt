package de.hsos.studcar.requestmanagement.boundary.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;

public class NewDriveOfferDTO {

    @NotNull
    @NotBlank
    public String startLocation;

    @NotNull
    @NotBlank
    public String pickUpLocation;

    @NotNull
    @NotBlank
    public String destinationCampus;

    @NotNull
    @NotBlank
    public String arrivalTime;

    @NotNull
    @NotBlank
    public String pickUpTime;

    @NotNull
    public Long freeSeats;

    public String description;

    public static DriveOffer mapDTOToDriveOffer(NewDriveOfferDTO dto) {
        return new DriveOffer(null, null, dto.startLocation, dto.pickUpLocation, dto.destinationCampus,
                dto.arrivalTime, dto.pickUpTime, dto.freeSeats, true, false, dto.description);
    }
}
