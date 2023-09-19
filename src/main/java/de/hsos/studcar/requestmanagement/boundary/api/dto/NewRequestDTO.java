package de.hsos.studcar.requestmanagement.boundary.api.dto;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.requestmanagement.entity.Request;

public class NewRequestDTO {

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

    public String description;

    public static Request mapDTOToRequest(NewRequestDTO dto) {
        return new Request(null, null, dto.pickUpLocation, dto.arrivalTime, dto.destinationCampus,
                dto.driveDate, true, dto.description, new ArrayList<>());
    }
}
