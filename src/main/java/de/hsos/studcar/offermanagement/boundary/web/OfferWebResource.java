package de.hsos.studcar.offermanagement.boundary.web;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.studcar.offermanagement.control.ManageOffers;
import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.shared.users.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("/view/offers")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class OfferWebResource {

    @Inject
    ManageOffers offerManager;

    @Inject
    JsonWebToken principal;

    @Inject
    UserService userService;

    private final Template page;

    public OfferWebResource(Template offers) {
        this.page = requireNonNull(offers, "page is required");
    }

    @GET
    public TemplateInstance getAllOffers(@QueryParam("sorting") String sorting,
            @QueryParam("driveDate") String driveDate,
            @QueryParam("destinationCampus") String destinationCampus, @QueryParam("seats") Integer seats) {

        String driverId = (String) principal.getClaim("id");

        Collection<Offer> offers = offerManager.selectAllOffers(sorting, driveDate,
                destinationCampus, seats);
        offers.forEach(o -> {
            o.setDriverId(userService.getUserName(o.getDriverId()));
            o.setFreeSeats(o.getRideRequests().stream().filter(r -> r.isAccepted()).count());
        });

        boolean newRequestsAvailable = offerManager.newRequestsAvailable(driverId);

        return page.data("newRequestsAvailable", newRequestsAvailable, "offers",
                offers);
    }
}
