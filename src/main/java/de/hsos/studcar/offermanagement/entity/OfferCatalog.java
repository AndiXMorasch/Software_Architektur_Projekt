package de.hsos.studcar.offermanagement.entity;

import java.util.Collection;

import de.hsos.studcar.offermanagement.entity.exceptions.AddRequestToInactiveOfferException;

public interface OfferCatalog {

    Collection<Offer> selectAllOffers();

    Collection<Offer> selectAllOffersByDriverId(String driverId);

    Offer selectOffer(Long offerId);

    Long createOffer(Offer offer);

    boolean modifyOffer(String driverId, Offer offer);

    boolean addRideRequestToOffer(RideRequest rideRequest, Long offerId) throws AddRequestToInactiveOfferException;

    boolean revokeOffer(String driverId, Long offerId);
}
