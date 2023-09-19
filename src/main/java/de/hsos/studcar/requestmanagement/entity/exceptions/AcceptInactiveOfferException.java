package de.hsos.studcar.requestmanagement.entity.exceptions;

public class AcceptInactiveOfferException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot change the accepted status of a DriveOffer, when the DriveOffer is already inactive.";

    public AcceptInactiveOfferException() {
        super(EXCEPTIONMESSAGE);
    }
}
