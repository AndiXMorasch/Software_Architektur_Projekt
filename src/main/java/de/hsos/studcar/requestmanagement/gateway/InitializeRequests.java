package de.hsos.studcar.requestmanagement.gateway;

import java.util.ArrayList;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.requestmanagement.entity.exceptions.AddOfferToInactiveRequestException;
import io.quarkus.runtime.StartupEvent;

@Singleton
public class InitializeRequests {

        @Inject
        RequestRepository repository;

        @Transactional
        public void initRequests(@Observes StartupEvent event) throws AddOfferToInactiveRequestException {

                Request request1 = new Request(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Borgholzhausen", "09:00",
                                "Lingen", "2024-07-26", true,
                                "Hey zusammen, kann mich am 26sten jemand vielleicht mitnehmen?", new ArrayList<>());
                DriveOffer driveOffer1 = new DriveOffer(null, "d0d200db-c04e-47e6-8324-7a11be6aee68", "Bielefeld",
                                "Borgholzhausen - Bahnhof", "Lingen",
                                "08:50",
                                "08:20", 4L, true, false,
                                "Moin, hätte noch Plätze frei. Ich kann dich am Bahnhof abholen. :)");
                DriveOffer driveOffer2 = new DriveOffer(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Halle (Westf.)",
                                "Borgholzhausen - Combi", "Lingen",
                                "08:45",
                                "08:10", 2L, true, false, "Servus - kannst gerne mit! ;)");
                DriveOffer driveOffer3 = new DriveOffer(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Gütersloh",
                                "Borgholzhausen - Bushaltestelle 'Am alten Markt'", "Lingen", "08:55",
                                "08:15", 3L, true, false, "Moin Moin - ich kann ich gerne mitnehmen!");

                Request request2 = new Request(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Ladbergen", "12:30",
                                "Westerberg", "2024-07-27", true,
                                "Leider ist mein Auto kurzfristig in der Werkstatt :-( ... kann mich wer mitnehmen?",
                                new ArrayList<>());
                DriveOffer driveOffer4 = new DriveOffer(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Emsdetten",
                                "Ladbergen - ZOB", "Westerberg",
                                "12:15",
                                "11:50", 1L, true, false, "Helloooo, kannst bei uns gerne mitfahren!!");
                DriveOffer driveOffer5 = new DriveOffer(null, "d0d200db-c04e-47e6-8324-7a11be6aee68", "Münster",
                                "Ladbergen - am OBI Parkplatz",
                                "Westerberg",
                                "12:20",
                                "12:00", 4L, true, true, "Sei dabei, wir haben Plätze frei! :)");

                Request request3 = new Request(null, "d0d200db-c04e-47e6-8324-7a11be6aee68", "Fürstenau", "10:45",
                                "Caprivi", "2024-07-28", true,
                                "Moinsen! Hätte wer Platz für einen humorvollen Typen mit dem es nicht langweilig wird? :D",
                                new ArrayList<>());
                DriveOffer driveOffer6 = new DriveOffer(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Lingen",
                                "Fürstenau - an der Eisdiele", "Caprivi",
                                "10:30",
                                "10:05", 3L, true, false,
                                "Humor schadet nie! Wenn du willst kannst du gerne bei uns mit.");
                DriveOffer driveOffer7 = new DriveOffer(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Lengerich",
                                "Fürstenau am Netto", "Westerberg",
                                "10:30",
                                "10:00", 2L, true, false,
                                "Grüß dich, wir suchen noch wen der uns zum lachen bringt. Du bist also jederzeit herzlich eingeladen!");

                Request request4 = new Request(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Lüstringen", "09:00",
                                "Westerberg", "2024-07-29", true,
                                "Moin Leute, mein Auto ist kaputt und die Busse fahren nicht. Kann mich vielleicht jemand mitnehmen?",
                                new ArrayList<>());
                DriveOffer driveOffer8 = new DriveOffer(null, "d0d200db-c04e-47e6-8324-7a11be6aee68", "Bielefeld",
                                "Abfahrt Lüstringen", "Westerberg",
                                "08:50",
                                "08:20", 4L, true, false,
                                "Moin, wenn du zur Autobahn kommst kann ich dich mitnehmen.");
                DriveOffer driveOffer9 = new DriveOffer(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Lüstringen",
                                "Lüstringen", "Westerberg",
                                "08:45",
                                "08:10", 2L, true, false,
                                "Moin, du kannst zu mir kommen und dann fahren wir zusammen.");

                Long request1Id = this.repository.createRequest(request1);
                this.repository.addDriveOfferToRequest(driveOffer1, request1Id);
                this.repository.addDriveOfferToRequest(driveOffer2, request1Id);
                this.repository.addDriveOfferToRequest(driveOffer3, request1Id);

                Long request2Id = this.repository.createRequest(request2);
                this.repository.addDriveOfferToRequest(driveOffer4, request2Id);
                this.repository.addDriveOfferToRequest(driveOffer5, request2Id);

                Long request3Id = this.repository.createRequest(request3);
                this.repository.addDriveOfferToRequest(driveOffer6, request3Id);
                this.repository.addDriveOfferToRequest(driveOffer7, request3Id);

                Long request4Id = this.repository.createRequest(request4);
                this.repository.addDriveOfferToRequest(driveOffer8, request4Id);
                this.repository.addDriveOfferToRequest(driveOffer9, request4Id);
        }
}
