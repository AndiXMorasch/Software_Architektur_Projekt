package de.hsos.studcar.requestmanagement.boundary.web;

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

import de.hsos.studcar.requestmanagement.control.ManageRequests;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.shared.users.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RequestScoped
@Path("/view/requests/user")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class OwnRequestsWebResource {

    @Inject
    ManageRequests requestManager;

    @Inject
    JsonWebToken principal;

    @Inject
    UserService userService;

    private final Template page;

    public OwnRequestsWebResource(Template ownrequests) {
        this.page = requireNonNull(ownrequests, "page is required");
    }

    @GET
    public TemplateInstance getAllRequests() {
        String passengerId = (String) principal.getClaim("id");
        Collection<Request> requests = requestManager.selectAllRequestsByPassengerId(passengerId);
        requests.forEach(r -> {
            r.setPassengerId(userService.getUserName(r.getPassengerId()));
            r.getDriveOffers().forEach(o -> o.setDriverId(userService.getUserName(o.getDriverId())));
        });
        return page.data("passengerId", passengerId, "ownRequests",
                requests);
    }
}
