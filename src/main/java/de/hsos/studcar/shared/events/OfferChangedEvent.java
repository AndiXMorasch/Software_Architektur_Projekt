package de.hsos.studcar.shared.events;

import java.util.HashMap;

public class OfferChangedEvent {
    public Long offerId;
    public String driverId;
    public String route;
    public String date;
    public HashMap<Long, String> requestIdsToPassengerIds;
}
