package de.hsos.studcar.requestmanagement.boundary.web;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.MediaType;

import de.hsos.studcar.requestmanagement.control.ManageRequests;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.shared.users.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("/view/requests/createDriveOffer")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class NewDriveOfferWebResource {

    @Inject
    ManageRequests requestManager;

    @Inject
    UserService userService;

    private final Template page;

    public NewDriveOfferWebResource(Template newdriveoffer) {
        this.page = requireNonNull(newdriveoffer, "page is required");
    }

    @GET
    public TemplateInstance getRequestId(@QueryParam("requestid") Long requestId) {
        Request request = this.requestManager.selectRequest(requestId);
        String passengerName = userService.getUserName(request.getPassengerId());
        return page.data("request", request, "passengerName", passengerName);
    }
}
