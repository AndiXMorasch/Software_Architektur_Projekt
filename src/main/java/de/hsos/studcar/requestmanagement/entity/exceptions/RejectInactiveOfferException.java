package de.hsos.studcar.requestmanagement.entity.exceptions;

public class RejectInactiveOfferException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot reject an inactive DriveOffer.";

    public RejectInactiveOfferException() {
        super(EXCEPTIONMESSAGE);
    }
}
