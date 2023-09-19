package de.hsos.studcar.offermanagement.boundary.api.dto;

import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.studcar.offermanagement.entity.Offer;

public class OfferDTO {

    @NotNull
    public Long id;

    public String driverId;

    @NotNull
    @NotBlank
    public String startLocation;

    @NotNull
    @NotBlank
    public String destinationCampus;

    @NotNull
    @NotBlank
    public String driveDate;

    @NotNull
    @NotBlank
    public String arrivalTime;

    @NotNull
    public Long freeSeats;

    @NotNull
    public boolean active;

    public String description;

    public Collection<RideRequestDTO> rideRequests;

    public static OfferDTO mapOfferToDTO(Offer offer) {
        OfferDTO dto = new OfferDTO();
        dto.id = offer.getId();
        dto.driverId = offer.getDriverId();
        dto.startLocation = offer.getStartLocation();
        dto.destinationCampus = offer.getDestinationCampus();
        dto.driveDate = offer.getDriveDate();
        dto.arrivalTime = offer.getArrivalTime();
        dto.freeSeats = offer.getFreeSeats();
        dto.active = offer.isActive();
        dto.description = offer.getDescription();
        dto.rideRequests = RideRequestDTO.mapRequestsToDTOs(offer.getRideRequests());
        return dto;
    }

    public static Offer mapDTOToOffer(OfferDTO dto) {
        Offer offer = new Offer();
        offer.setId(dto.id);
        offer.setDriverId(dto.driverId);
        offer.setStartLocation(dto.startLocation);
        offer.setDestinationCampus(dto.destinationCampus);
        offer.setDriveDate(dto.driveDate);
        offer.setArrivalTime(dto.arrivalTime);
        offer.setFreeSeats(dto.freeSeats);
        offer.setActive(dto.active);
        offer.setDescription(dto.description);
        return offer;
    }
}
