package de.hsos.studcar.offermanagement;

import org.junit.jupiter.api.Test;

import de.hsos.studcar.MockToken;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

@QuarkusTest
public class RideRequestResourceTest {

        @Inject
        JsonWebToken principle;

        @Test
        public void testGetAllRideRequests() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .when().get("/offers/riderequests")
                                .then()
                                .statusCode(200)
                                .body(
                                                // wird potentiell modifiziert
                                                // "[0].passengerId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                // "[0].pickUpLocation", is("Melle"),
                                                // "[0].pickUpTime", is("07:50"),
                                                // "[0].destinationCampus", is("Westerberg"),
                                                // "[0].active", is(true),
                                                // "[0].accepted", is(false),
                                                // "[0].description",
                                                // is("Hey würde gerne mitfahren."),

                                                "[1].passengerId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "[1].pickUpLocation", is("Versmold"),
                                                "[1].pickUpTime", is("07:30"),
                                                "[1].destinationCampus", is("Westerberg"),
                                                "[1].active", is(true),
                                                "[1].accepted", is(false),
                                                "[1].description",
                                                is("Grüß dich, fährst du auch an Versmold vorbei und hättest noch einen Sitz frei?"),

                                                "[2].passengerId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "[2].pickUpLocation", is("Ibbenbüren"),
                                                "[2].pickUpTime", is("07:40"),
                                                "[2].destinationCampus", is("Caprivi"),
                                                "[2].active", is(true),
                                                "[2].accepted", is(false),
                                                "[2].description",
                                                is("Ein Platz frei... uiuiui, darf ich mit? :)"));
        }

        @Test
        public void testModifyRideRequest() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .body("{\"id\": 1, \"pickUpLocation\": \"Versmold\", \"pickUpTime\": \"08:10\", \"destinationCampus\": \"Lingen\", "
                                                +
                                                "\"active\": true, \"accepted\": true, \"description\": \"Hey Alice! Ich würde gerne mitfahren! :)\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/offers/riderequests/1")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/offers/riderequests/1")
                                .then()
                                .statusCode(200)
                                .body("passengerId", is("eb4123a3-b722-4798-9af5-8957f823657a"),
                                                "pickUpLocation", is("Versmold"),
                                                "pickUpTime", is("08:10"),
                                                "destinationCampus", is("Lingen"),
                                                "active", is(true),
                                                "accepted", is(true),
                                                "description", is("Hey Alice! Ich würde gerne mitfahren! :)"));
        }

        @Test
        public void testAcceptRiderequest() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/offers/riderequests/4/accept")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/offers/riderequests/4")
                                .then()
                                .statusCode(200)
                                .body("accepted", is(true));
        }

        @Test
        public void testRevokeRequest() {
                QuarkusMock.installMockForInstance(new MockToken(), principle);
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .delete("/offers/riderequests/7")
                                .then()
                                .statusCode(200);

                given()
                                .when().get("offers/riderequests/7")
                                .then()
                                .statusCode(200)
                                .body("active", is(false));
        }
}
