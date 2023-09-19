package de.hsos.studcar.user.boundary.web;

import static java.util.Objects.requireNonNull;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.studcar.shared.users.User;
import de.hsos.studcar.shared.users.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@RequestScoped
@Path("/view/user")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class UserWebResource {

    private final Template page;

    @Inject
    UserService userService;

    @Inject
    JsonWebToken principal;

    public UserWebResource(Template user) {
        this.page = requireNonNull(user, "page is required");
    }

    @GET
    public TemplateInstance getEmptyNewOfferCreationPage() {
        String userId = (String) principal.getClaim("id");
        User user = this.userService.getUser(userId);

        return page.data("user", user);
    }
}