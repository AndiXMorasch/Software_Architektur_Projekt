package de.hsos.studcar.requestmanagement.boundary.web;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.studcar.requestmanagement.control.ManageRequests;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.shared.users.UserService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RequestScoped
@Path("/view/requests")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class RequestWebResource {

    @Inject
    ManageRequests requestManager;

    @Inject
    UserService userService;

    @Inject
    JsonWebToken principal;

    private final Template page;

    public RequestWebResource(Template requests) {
        this.page = requireNonNull(requests, "page is required");
    }

    @GET
    public TemplateInstance getAllRequests(@QueryParam("sorting") String sorting,
            @QueryParam("driveDate") String driveDate,
            @QueryParam("destinationCampus") String destinationCampus) {

        String passengerId = (String) principal.getClaim("id");

        Collection<Request> requests = requestManager.selectAllRequests(sorting, driveDate,
                destinationCampus);
        requests.forEach(r -> r.setPassengerId(userService.getUserName(r.getPassengerId())));

        boolean newOffersAvailable = requestManager.newOffersAvailable(passengerId);

        return page.data("newOffersAvailable", newOffersAvailable, "requests",
                requests);
    }
}
