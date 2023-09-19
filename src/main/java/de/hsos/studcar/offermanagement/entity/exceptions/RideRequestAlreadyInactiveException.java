package de.hsos.studcar.offermanagement.entity.exceptions;

public class RideRequestAlreadyInactiveException extends Exception {

    public final static String EXCEPTIONMESSAGE = "You cannot revoke an inactive RideRequest.";

    public RideRequestAlreadyInactiveException() {
        super(EXCEPTIONMESSAGE);
    }
}
