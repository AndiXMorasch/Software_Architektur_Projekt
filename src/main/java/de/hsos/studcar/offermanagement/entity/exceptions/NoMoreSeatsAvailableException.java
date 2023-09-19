package de.hsos.studcar.offermanagement.entity.exceptions;

public class NoMoreSeatsAvailableException extends Exception {

    public final static String EXCEPTIONMESSAGE = "There are no more seats available in the offer.";

    public NoMoreSeatsAvailableException() {
        super(EXCEPTIONMESSAGE);
    }
}
