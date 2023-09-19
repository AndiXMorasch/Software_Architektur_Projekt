package de.hsos.studcar.offermanagement;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;

import de.hsos.studcar.MockToken;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

@QuarkusTest
public class OfferResourceTest {

        @Inject
        JsonWebToken principle;

        @Test
        public void testGetAllOffers() {
                given()
                                .when().get("/offers")
                                .then()
                                .statusCode(200)
                                .body(
                                                "[0].driverId", is("1eed6a8e-a853-4597-b4c6-c4c2533546a0"),
                                                "[0].startLocation", is("Bielefeld"),
                                                "[0].destinationCampus", is("Westerberg"),
                                                "[0].driveDate", is("2024-07-26"),
                                                "[0].arrivalTime", is("09:45"),
                                                "[0].freeSeats", is(4),
                                                "[0].active", is(true),
                                                "[0].description", is("Biete Mitfahrgelegenheit an! :)"),

                                                // wird potentiell modifiziert
                                                // "[1].driverId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                // "[1].startLocation", is("Lingen"),
                                                // "[1].destinationCampus", is("Caprivi"),
                                                // "[1].driveDate", is("2024-07-27"),
                                                // "[1].arrivalTime", is("11:30"),
                                                // "[1].freeSeats", is(3),
                                                // "[1].active", is(true),
                                                // "[1].description", is("Sei dabei, es sind Plätze frei."),

                                                "[2].driverId", is("d0d200db-c04e-47e6-8324-7a11be6aee68"),
                                                "[2].startLocation", is("Rheine"),
                                                "[2].destinationCampus", is("Westerberg"),
                                                "[2].driveDate", is("2024-08-07"),
                                                "[2].arrivalTime", is("08:00"),
                                                "[2].freeSeats", is(5),
                                                "[2].active", is(true),
                                                "[2].description", is("Come along! Nur noch ein Platz frei!!!"));
        }

        @Test
        public void testCreateOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"startLocation\": \"Herford\", \"destinationCampus\": \"Lingen\", "
                                                + "\"driveDate\": \"2024-08-09\", \"arrivalTime\": \"08:37\", \"freeSeats\": 2, \"description\": "
                                                + "\"Biete Mitfahrgelegenheit an! Steigt gerne ein.\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/offers")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/offers/5")
                                .then()
                                .statusCode(200)
                                .body("id", is(5),
                                                "startLocation", is("Herford"),
                                                "destinationCampus", is("Lingen"),
                                                "driveDate", is("2024-08-09"),
                                                "arrivalTime", is("08:37"),
                                                "freeSeats", is(2),
                                                "active", is(true),
                                                "description",
                                                is("Biete Mitfahrgelegenheit an! Steigt gerne ein."));
        }

        @Test
        public void testModifyOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"id\": 2, \"startLocation\": \"Melle\", \"destinationCampus\": \"Caprivi\", "
                                                +
                                                "\"driveDate\": \"2024-08-01\", \"arrivalTime\": \"10:37\", \"freeSeats\": 3, \"active\": true, \"description\": \"Biete Mitfahrgelegenheit an! Steigt gerne ein.\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/offers/2")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/offers/2")
                                .then()
                                .statusCode(200)
                                .body("driverId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "startLocation", is("Melle"),
                                                "destinationCampus", is("Caprivi"),
                                                "driveDate", is("2024-08-01"),
                                                "arrivalTime", is("10:37"),
                                                "freeSeats", is(3),
                                                "active", is(true),
                                                "description", is("Biete Mitfahrgelegenheit an! Steigt gerne ein."));

        }

        @Test
        public void testAddRideRequest() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"pickUpLocation\": \"Gütersloh\", \"pickUpTime\": \"10:45\", \"destinationCampus\": \"Caprivi\","
                                                + "\"description\": \"Hey würde gerne mit, ginge das? Beste Grüße, Anton\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/offers/2")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("offers/2")
                                .then()
                                .statusCode(200)
                                .body("rideRequests[2].pickUpLocation", is("Gütersloh"),
                                                "rideRequests[2].pickUpTime", is("10:45"),
                                                "rideRequests[2].destinationCampus", is("Caprivi"),
                                                "rideRequests[2].description",
                                                is("Hey würde gerne mit, ginge das? Beste Grüße, Anton"));
        }

        @Test
        public void testRevokeOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .delete("/offers/4")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("offers/4")
                                .then()
                                .statusCode(404)
                                .body(is("There is no offer with this offerId."));
        }

}
