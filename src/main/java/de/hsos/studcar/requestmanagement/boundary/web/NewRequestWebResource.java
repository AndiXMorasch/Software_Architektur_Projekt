package de.hsos.studcar.requestmanagement.boundary.web;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RequestScoped
@Path("/view/requests/create")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
public class NewRequestWebResource {

    private final Template page;

    public NewRequestWebResource(Template newrequest) {
        this.page = requireNonNull(newrequest, "page is required");
    }

    @GET
    public TemplateInstance getEmptyNewRequestCreationPage() {
        return page.data(null);
    }
}
