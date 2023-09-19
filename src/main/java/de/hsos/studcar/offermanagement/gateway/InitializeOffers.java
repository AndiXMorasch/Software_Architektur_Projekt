package de.hsos.studcar.offermanagement.gateway;

import java.util.ArrayList;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.exceptions.AddRequestToInactiveOfferException;
import io.quarkus.runtime.StartupEvent;

@Singleton
public class InitializeOffers {

        @Inject
        OfferRepository repository;

        @Transactional
        public void initOffers(@Observes StartupEvent event) throws AddRequestToInactiveOfferException {
                Offer offer1 = new Offer(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Bielefeld", "Westerberg",
                                "2024-07-26", "09:45",
                                4L,
                                true,
                                "Biete Mitfahrgelegenheit an! :)", new ArrayList<>());
                RideRequest rideRequest1 = new RideRequest(null,
                                "eb4123a3-b722-4798-9af5-8957f823657a", "Melle", "07:50", "Westerberg", true, false,
                                "Hey würde gerne mitfahren.");
                RideRequest rideRequest2 = new RideRequest(null,
                                "d0d200db-c04e-47e6-8324-7a11be6aee68", "Wissingen", "08:20", "Westerberg", true,
                                false,
                                "Moinsen, hättest du noch Platz?");
                RideRequest rideRequest3 = new RideRequest(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Versmold",
                                "07:30", "Westerberg", true, false,
                                "Grüß dich, fährst du auch an Versmold vorbei und hättest noch einen Sitz frei?");

                Offer offer2 = new Offer(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Lingen", "Caprivi",
                                "2024-07-27", "11:30", 3L,
                                true,
                                "Sei dabei, es sind Plätze frei.", new ArrayList<>());
                RideRequest rideRequest4 = new RideRequest(null,
                                "d0d200db-c04e-47e6-8324-7a11be6aee68", "Neuenkirchen", "10:55", "Caprivi", true,
                                false,
                                "Heyo, nimmst du mich mit?");
                RideRequest rideRequest5 = new RideRequest(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Bramsche",
                                "11:10", "Caprivi", true, true,
                                "Würde gerne mitfahren, geht das?");

                Offer offer3 = new Offer(null,
                                "d0d200db-c04e-47e6-8324-7a11be6aee68", "Rheine", "Westerberg",
                                "2024-08-07", "08:00",
                                5L,
                                true,
                                "Come along! Nur noch ein Platz frei!!!", new ArrayList<>());
                RideRequest rideRequest6 = new RideRequest(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Hörstel",
                                "07:30", "Caprivi", true, false,
                                "Hey würde gerne mitfahren! :D");
                RideRequest rideRequest7 = new RideRequest(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Ibbenbüren",
                                "07:40", "Caprivi", true, false,
                                "Ein Platz frei... uiuiui, darf ich mit? :)");

                Offer offer4 = new Offer(null, "eb4123a3-b722-4798-9af5-8957f823657a", "Hasbergen", "Haste",
                                "2024-08-08", "11:30", 3L,
                                true,
                                "Ausnahmsweise nehme ich mal das Auto.", new ArrayList<>());
                RideRequest rideRequest8 = new RideRequest(null, "1eed6a8e-a853-4597-b4c6-c4c2533546a0", "Hellern",
                                "07:30", "Haste", true, false,
                                "Hey würde gerne mitfahren, weil die Busse streiken.");
                RideRequest rideRequest9 = new RideRequest(null, "d0d200db-c04e-47e6-8324-7a11be6aee68", "Hagen",
                                "07:40", "Haste", true, false,
                                "Ich weiß es ist ein kleiner Umweg, aber ich würde dir auch Geld geben.");

                Long offer1Id = this.repository.createOffer(offer1);
                this.repository.addRideRequestToOffer(rideRequest1, offer1Id);
                this.repository.addRideRequestToOffer(rideRequest2, offer1Id);
                this.repository.addRideRequestToOffer(rideRequest3, offer1Id);

                Long offer2Id = this.repository.createOffer(offer2);
                this.repository.addRideRequestToOffer(rideRequest4, offer2Id);
                this.repository.addRideRequestToOffer(rideRequest5, offer2Id);

                Long offer3Id = this.repository.createOffer(offer3);
                this.repository.addRideRequestToOffer(rideRequest6, offer3Id);
                this.repository.addRideRequestToOffer(rideRequest7, offer3Id);

                Long offer4Id = this.repository.createOffer(offer4);
                this.repository.addRideRequestToOffer(rideRequest8, offer4Id);
                this.repository.addRideRequestToOffer(rideRequest9, offer4Id);
        }
}
