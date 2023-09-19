package de.hsos.studcar.requestmanagement.boundary.api;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.transaction.Transactional.TxType;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.studcar.requestmanagement.boundary.api.dto.NewDriveOfferDTO;
import de.hsos.studcar.requestmanagement.boundary.api.dto.NewRequestDTO;
import de.hsos.studcar.requestmanagement.boundary.api.dto.RequestDTO;
import de.hsos.studcar.requestmanagement.control.ManageRequests;
import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.Request;
import de.hsos.studcar.requestmanagement.entity.exceptions.AddOfferToInactiveRequestException;

@RequestScoped
@Transactional(TxType.REQUIRES_NEW)
@Path("/requests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RequestResource {

    private final String NOTFOUNDMESSAGE = "There is no request with this requestId.";

    @Inject
    ManageRequests manageRequests;

    @Inject
    JsonWebToken principal;

    @GET
    public Response getAllRequests(@QueryParam("sorting") String sorting,
            @QueryParam("driveDate") String driveDate, @QueryParam("destinationCampus") String destinationCampus) {

        Collection<Request> requests = this.manageRequests.selectAllRequests(sorting, driveDate,
                destinationCampus);
        Collection<RequestDTO> dtos = requests.stream().map(RequestDTO::mapRequestToDTO).toList();
        return Response.ok()
                .entity(dtos)
                .build();
    }

    @POST
    public Response createNewRequest(@Valid NewRequestDTO dto) {

        String passengerId = (String) principal.getClaim("id");

        Long requestId = this.manageRequests.createRequest(passengerId,
                NewRequestDTO.mapDTOToRequest(dto));
        if (requestId != null) {
            return Response.ok(requestId).build();
        }

        // Internal Server Error wenn ein neuer Request nicht angelegt werden kann,
        // bis hierhin darf das Progamm im Optimalfall nicht kommen
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{requestId}")
    public Response getRequestDetails(@PathParam("requestId") Long requestId) {
        Request request = this.manageRequests.selectRequest(requestId);

        if (request != null) {
            RequestDTO dto = RequestDTO.mapRequestToDTO(request);
            return Response.ok(dto).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @PUT
    @Path("/{requestId}")
    public Response modifyRequest(@Valid RequestDTO dto) {

        try {
            String passengerId = (String) principal.getClaim("id");

            Request request = RequestDTO.mapDTOToRequest(dto);
            boolean modified = this.manageRequests.modifyRequest(passengerId, request);
            if (modified) {
                return Response.ok().build();
            }
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @POST
    @Path("/{requestId}")
    public Response addDriveOfferToRequest(@PathParam("requestId") Long requestId, @Valid NewDriveOfferDTO dto) {

        Request request = this.manageRequests.selectRequest(requestId);
        boolean wasAdded = false;
        String driverId = (String) principal.getClaim("id");

        try {
            if (request != null) {
                DriveOffer driveOffer = NewDriveOfferDTO.mapDTOToDriveOffer(dto);
                driveOffer.setDriverId(driverId);
                wasAdded = this.manageRequests.addDriveOfferToRequest(driveOffer, requestId);
            }
        } catch (AddOfferToInactiveRequestException e) {
            return Response.notModified().entity(AddOfferToInactiveRequestException.EXCEPTIONMESSAGE).build();
        }

        if (wasAdded) {
            return Response.ok().build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @DELETE
    @Path("/{requestId}")
    public Response revokeRequest(@PathParam("requestId") Long requestId) {
        try {
            String passengerId = (String) principal.getClaim("id");

            boolean deleted = this.manageRequests.revokeRequest(passengerId, requestId);

            if (deleted) {
                return Response.ok().build();
            }
        } catch (

        SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }
}
