package de.hsos.studcar.requestmanagement;

import org.junit.jupiter.api.Test;

import de.hsos.studcar.MockToken;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

@QuarkusTest
public class DriveOfferResourceTest {

        @Inject
        JsonWebToken principle;

        @Test
        public void testGetAllDriveOffers() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .when().get("/requests/driveoffers")
                                .then()
                                .statusCode(200)
                                .body(
                                                // wird potentiell modifiziert
                                                // "[0].driverId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                // "[0].startLocation", is("Emsdetten"),
                                                // "[0].pickUpLocation", is("Ladbergen - ZOB"),
                                                // "[0].destinationCampus", is("Westerberg"),
                                                // "[0].arrivalTime", is("12:15"),
                                                // "[0].pickUpTime", is("11:50"),
                                                // "[0].freeSeats", is(1),
                                                // "[0].active", is(true),
                                                // "[0].accepted", is(false),
                                                // "[0].description", is("Helloooo, kannst bei uns gerne mitfahren!!"),

                                                "[1].driverId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "[1].startLocation", is("Lengerich"),
                                                "[1].pickUpLocation", is("Fürstenau am Netto"),
                                                "[1].destinationCampus", is("Westerberg"),
                                                "[1].arrivalTime", is("10:30"),
                                                "[1].pickUpTime", is("10:00"),
                                                "[1].freeSeats", is(2),
                                                // "[1].active", is(true), eventuell bereits deaktiviert
                                                "[1].accepted", is(false),
                                                "[1].description",
                                                is("Grüß dich, wir suchen noch wen der uns zum lachen bringt. Du bist also jederzeit herzlich eingeladen!"));
        }

        @Test
        public void testModifyDriveOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"id\": 4, \"startLocation\": \"Emsdetten\", \"pickUpLocation\": \"Emsdetten\", \"destinationCampus\": \"Lingen\", "
                                                +
                                                "\"arrivalTime\": \"10:00\", \"pickUpTime\": \"09:15\", \"freeSeats\": 3, "
                                                +
                                                "\"active\": true, \"accepted\": true, \"description\": \"Hey Tom! Ich kann dich gerne mitnehmen! :)\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/requests/driveoffers/4")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/requests/driveoffers/4")
                                .then()
                                .statusCode(200)
                                .body("driverId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "startLocation", is("Emsdetten"),
                                                "pickUpLocation", is("Emsdetten"),
                                                "destinationCampus", is("Lingen"),
                                                "arrivalTime", is("10:00"),
                                                "pickUpTime", is("09:15"),
                                                "freeSeats", is(3),
                                                "active", is(true),
                                                "accepted", is(true),
                                                "description", is("Hey Tom! Ich kann dich gerne mitnehmen! :)"));
        }

        @Test
        public void testAcceptDriveOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/requests/driveoffers/1/accept")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/requests/driveoffers/1")
                                .then()
                                .statusCode(200)
                                .body("accepted", is(true));
        }

        @Test
        public void testRevokeOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .delete("/requests/driveoffers/7")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/requests/driveoffers/7")
                                .then()
                                .statusCode(200)
                                .body("active", is(false));
        }
}
