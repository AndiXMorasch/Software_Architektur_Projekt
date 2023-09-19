package de.hsos.studcar.requestmanagement;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;

import de.hsos.studcar.MockToken;

@QuarkusTest
public class RequestResourceTest {

        @Inject
        JsonWebToken principle;

        @Test
        public void testGetAllRequests() {
                given()
                                .when().get("/requests")
                                .then()
                                .statusCode(200)
                                .body(
                                                // wird potentiell modifiziert
                                                // "[0].passengerId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                // "[0].pickUpLocation", is("Borgholzhausen"),
                                                // "[0].arrivalTime", is("09:00"),
                                                // "[0].destinationCampus", is("Lingen"),
                                                // "[0].driveDate", is("2024-07-26"),
                                                // "[0].active", is(true),
                                                // "[0].description",
                                                // is("Hey zusammen, kann mich am 26sten jemand vielleicht mitnehmen?"),

                                                "[1].passengerId", is("1eed6a8e-a853-4597-b4c6-c4c2533546a0"),
                                                "[1].pickUpLocation", is("Ladbergen"),
                                                "[1].arrivalTime", is("12:30"),
                                                "[1].destinationCampus", is("Westerberg"),
                                                "[1].driveDate", is("2024-07-27"),
                                                "[1].active", is(true),
                                                "[1].description",
                                                is("Leider ist mein Auto kurzfristig in der Werkstatt :-( ... kann mich wer mitnehmen?"),

                                                "[2].passengerId", is("d0d200db-c04e-47e6-8324-7a11be6aee68"),
                                                "[2].pickUpLocation", is("Fürstenau"),
                                                "[2].arrivalTime", is("10:45"),
                                                "[2].destinationCampus", is("Caprivi"),
                                                "[2].driveDate", is("2024-07-28"),
                                                "[2].active", is(true),
                                                "[2].description",
                                                is("Moinsen! Hätte wer Platz für einen humorvollen Typen mit dem es nicht langweilig wird? :D"));
        }

        @Test
        public void testCreateRequest() {
                given()
                                .body("{\"pickUpLocation\": \"Herford\", \"arrivalTime\": \"08:55\", \"destinationCampus\": \"Westerberg\", "
                                                +
                                                "\"driveDate\": \"2024-08-03\", \"description\": \"Kann mich am 03.08. bitte irgendwer mitnehmen? Lieben Dank!\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/requests")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/requests/5")
                                .then()
                                .statusCode(200)
                                .body(
                                                "pickUpLocation", is("Herford"),
                                                "arrivalTime", is("08:55"),
                                                "destinationCampus", is("Westerberg"),
                                                "driveDate", is("2024-08-03"),
                                                "description",
                                                is("Kann mich am 03.08. bitte irgendwer mitnehmen? Lieben Dank!"));
        }

        @Test
        public void testModifyRequest() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"id\": 1, \"pickUpLocation\": \"Versmold\", \"arrivalTime\": \"11:50\", \"destinationCampus\": \"Lingen\", "
                                                +
                                                "\"driveDate\": \"2024-07-25\", \"active\": true, \"description\": \"Moin Klaus, würde gerne mitfahren. Beste Grüße!\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/requests/1")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/requests/1")
                                .then()
                                .statusCode(200)
                                .body("passengerId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "pickUpLocation", is("Versmold"),
                                                "destinationCampus", is("Lingen"),
                                                "driveDate", is("2024-07-25"),
                                                "arrivalTime", is("11:50"),
                                                "active", is(true),
                                                "description",
                                                is("Moin Klaus, würde gerne mitfahren. Beste Grüße!"));
        }

        @Test
        public void testAddDriveOffer() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"startLocation\": \"Paderborn\", \"pickUpLocation\": \"Brockhagen\", \"destinationCampus\": \"Westerberg\","
                                                + "\"arrivalTime\": \"09:30\", \"pickUpTime\": \"09:00\", \"freeSeats\": 2, "
                                                + "\"description\": \"Hey würde gerne mit, ginge das? Beste Grüße, Anton\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/requests/1")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("requests/1")
                                .then()
                                .statusCode(200)
                                .body("driveOffers[3].startLocation", is("Paderborn"),
                                                "driveOffers[3].pickUpLocation", is("Brockhagen"),
                                                "driveOffers[3].destinationCampus", is("Westerberg"),
                                                "driveOffers[3].arrivalTime", is("09:30"),
                                                "driveOffers[3].pickUpTime", is("09:00"),
                                                "driveOffers[3].freeSeats", is(2),
                                                "driveOffers[3].description",
                                                is("Hey würde gerne mit, ginge das? Beste Grüße, Anton"));
        }

        @Test
        public void testRevokeRequest() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .delete("/requests/4")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("requests/4")
                                .then()
                                .statusCode(404)
                                .body(is("There is no request with this requestId."));
        }

}
