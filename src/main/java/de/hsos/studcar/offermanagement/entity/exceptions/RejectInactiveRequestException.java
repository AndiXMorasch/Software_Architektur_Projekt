package de.hsos.studcar.offermanagement.entity.exceptions;

public class RejectInactiveRequestException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot reject an inactive RideRequest.";

    public RejectInactiveRequestException() {
        super(EXCEPTIONMESSAGE);
    }
}
