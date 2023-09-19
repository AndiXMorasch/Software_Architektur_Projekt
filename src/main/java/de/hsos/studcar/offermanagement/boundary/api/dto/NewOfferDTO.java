package de.hsos.studcar.offermanagement.boundary.api.dto;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import de.hsos.studcar.offermanagement.entity.Offer;

public class NewOfferDTO {

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

    public String description;

    public static Offer mapDTOToOffer(NewOfferDTO dto) {
        Offer offer = new Offer();

        offer.setStartLocation(dto.startLocation);
        offer.setDestinationCampus(dto.destinationCampus);
        offer.setDriveDate(dto.driveDate);
        offer.setFreeSeats(dto.freeSeats);
        offer.setDescription(dto.description);
        offer.setActive(true);
        offer.setArrivalTime(dto.arrivalTime);
        offer.setRideRequests(new ArrayList<>());

        return offer;
    }
}
