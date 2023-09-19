package de.hsos.studcar.offermanagement.entity.exceptions;

public class AddRequestToInactiveOfferException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot add a RideRequest to an offer which is inactive.";

    public AddRequestToInactiveOfferException() {
        super(EXCEPTIONMESSAGE);
    }
}
