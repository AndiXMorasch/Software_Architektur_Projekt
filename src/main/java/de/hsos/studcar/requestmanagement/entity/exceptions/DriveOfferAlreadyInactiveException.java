package de.hsos.studcar.requestmanagement.entity.exceptions;

public class DriveOfferAlreadyInactiveException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot revoke an inactive DriveOffer.";

    public DriveOfferAlreadyInactiveException() {
        super(EXCEPTIONMESSAGE);
    }
}
