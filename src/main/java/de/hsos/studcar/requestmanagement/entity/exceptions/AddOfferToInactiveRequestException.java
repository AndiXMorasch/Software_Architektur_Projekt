package de.hsos.studcar.requestmanagement.entity.exceptions;

public class AddOfferToInactiveRequestException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot add a DriveOffer to a request that is already inactive.";

    public AddOfferToInactiveRequestException() {
        super(EXCEPTIONMESSAGE);
    }
}
