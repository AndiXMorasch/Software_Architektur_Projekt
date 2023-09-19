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

@RequestScoped
@Path("/view/offers/user")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class OwnOffersWebResource {

    @Inject
    ManageOffers offerManager;

    @Inject
    JsonWebToken principal;

    @Inject
    UserService userService;

    private final Template page;

    public OwnOffersWebResource(Template ownoffers) {
        this.page = requireNonNull(ownoffers, "page is required");
    }

    @GET
    public TemplateInstance getAllOffers() {
        String driverId = (String) principal.getClaim("id");

        Collection<Offer> offers = offerManager.selectAllOffersByDriverId(driverId);
        offers.forEach(o -> {
            o.setDriverId(userService.getUserName(o.getDriverId()));
            o.setFreeSeats(o.getFreeSeats() - o.getRideRequests().stream().filter(r -> r.isAccepted()).count());
            o.getRideRequests().forEach(r -> r.setPassengerId(userService.getUserName(r.getPassengerId())));
        });

        return page.data("driverId", driverId, "ownOffers",
                offers);
    }
}
