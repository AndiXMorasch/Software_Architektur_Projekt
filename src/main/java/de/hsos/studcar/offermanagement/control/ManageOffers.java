package de.hsos.studcar.offermanagement.control;

import java.util.Collection;

import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.exceptions.AddRequestToInactiveOfferException;

public interface ManageOffers {

    Collection<Offer> selectAllOffers(String sorting, String driveDate, String destinationCampus,
            Integer freeSeats);

    Collection<Offer> selectAllOffersByDriverId(String driverId);

    Offer selectOffer(Long offerId);

    boolean newRequestsAvailable(String driverId);

    Long createOffer(String driverId, Offer offer);

    boolean modifyOffer(String driverId, Offer offer);

    boolean addRideRequestToOffer(RideRequest rideRequest, Long offerId) throws AddRequestToInactiveOfferException;

    boolean revokeOffer(String driverId, Long offerId);
}
