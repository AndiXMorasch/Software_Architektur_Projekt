package de.hsos.studcar.offermanagement.entity.exceptions;

public class AcceptInactiveRequestException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot change the accepted status of a RideRequest, when the RideRequest is already inactive.";

    public AcceptInactiveRequestException() {
        super(EXCEPTIONMESSAGE);
    }
}
