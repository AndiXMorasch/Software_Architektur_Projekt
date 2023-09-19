package de.hsos.studcar.offermanagement.boundary.web;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.MediaType;

import de.hsos.studcar.offermanagement.control.ManageOffers;
import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.shared.users.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("/view/offers/createRideRequest")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class NewRideRequestWebResource {

    @Inject
    ManageOffers offerManager;

    @Inject
    UserService userService;

    private final Template page;

    public NewRideRequestWebResource(Template newriderequest) {
        this.page = requireNonNull(newriderequest, "page is required");
    }

    @GET
    public TemplateInstance getOfferId(@QueryParam("offerid") Long offerId) {
        Offer offer = this.offerManager.selectOffer(offerId);
        String driverName = this.userService.getUserName(offer.getDriverId());
        return page.data("offer", offer, "driverName", driverName);
    }
}
