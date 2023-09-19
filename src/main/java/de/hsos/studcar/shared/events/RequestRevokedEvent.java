package de.hsos.studcar.shared.events;

import java.util.HashMap;

public class RequestRevokedEvent {
    public Long requestId;
    public String passengerId;
    public String route;
    public String date;
    public HashMap<Long, String> offerIdsToDriverIds;
}
