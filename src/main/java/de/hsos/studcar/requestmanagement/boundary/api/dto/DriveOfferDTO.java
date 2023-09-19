package de.hsos.studcar.requestmanagement.boundary.api.dto;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;

public class DriveOfferDTO {

    @NotNull
    public Long id;

    public String driverId;

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

    @NotNull
    public boolean active;

    @NotNull
    public boolean accepted;

    public String description;

    public static DriveOfferDTO mapDriveOfferToDTO(DriveOffer driveOffer) {
        DriveOfferDTO dto = new DriveOfferDTO();
        dto.id = driveOffer.getId();
        dto.driverId = driveOffer.getDriverId();
        dto.startLocation = driveOffer.getStartLocation();
        dto.pickUpLocation = driveOffer.getPickUpLocation();
        dto.destinationCampus = driveOffer.getDestinationCampus();
        dto.arrivalTime = driveOffer.getArrivalTime();
        dto.pickUpTime = driveOffer.getPickUpTime();
        dto.freeSeats = driveOffer.getFreeSeats();
        dto.active = driveOffer.isActive();
        dto.accepted = driveOffer.isAccepted();
        dto.description = driveOffer.getDescription();
        return dto;
    }

    public static DriveOffer mapDTOToDriveOffer(DriveOfferDTO dto) {
        return new DriveOffer(dto.id, dto.driverId, dto.startLocation, dto.pickUpLocation, dto.destinationCampus,
                dto.arrivalTime, dto.pickUpTime, dto.freeSeats, dto.active, dto.accepted, dto.description);
    }

    public static Collection<DriveOfferDTO> mapDriveOffersToDTOs(Collection<DriveOffer> offers) {
        Collection<DriveOfferDTO> dtos = new ArrayList<>();
        offers.stream().forEach(o -> dtos.add(mapDriveOfferToDTO(o)));
        return dtos;
    }

    public static Collection<DriveOffer> mapDTOsToDriveOffers(Collection<DriveOfferDTO> dtos) {
        Collection<DriveOffer> offers = new ArrayList<>();
        dtos.stream().forEach(o -> offers.add(mapDTOToDriveOffer(o)));
        return offers;
    }
}
