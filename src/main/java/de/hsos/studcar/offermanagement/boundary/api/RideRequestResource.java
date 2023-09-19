package de.hsos.studcar.offermanagement.boundary.api;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.studcar.offermanagement.boundary.api.dto.RideRequestDTO;
import de.hsos.studcar.offermanagement.control.ManageRideRequests;
import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.exceptions.AcceptInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.NoMoreSeatsAvailableException;
import de.hsos.studcar.offermanagement.entity.exceptions.RejectInactiveRequestException;
import de.hsos.studcar.offermanagement.entity.exceptions.RideRequestAlreadyInactiveException;

import javax.transaction.Transactional.TxType;
import javax.validation.Valid;

@RequestScoped
@Transactional(TxType.REQUIRES_NEW)
@Path("/offers/riderequests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RideRequestResource {

    private final String NOTFOUNDMESSAGE = "There is no RideRequest with this requestId.";

    @Inject
    ManageRideRequests manageRideRequests;

    @Inject
    JsonWebToken principal;

    @GET
    public Response getAllRideRequests() {

        String passengerId = (String) principal.getClaim("id");

        Collection<RideRequest> requests = this.manageRideRequests.selectAllRideRequests(passengerId);
        Collection<RideRequestDTO> dtos = requests.stream().map(RideRequestDTO::mapRideRequestToDTO).toList();
        return Response.ok()
                .entity(dtos)
                .build();
    }

    @GET
    @Path("/{requestId}")
    public Response getRideRequestDetails(@PathParam("requestId") Long requestId) {

        String passengerId = (String) principal.getClaim("id");

        RideRequest rideRequest = this.manageRideRequests.selectRideRequest(passengerId, requestId);

        if (rideRequest != null) {
            RideRequestDTO dto = RideRequestDTO.mapRideRequestToDTO(rideRequest);
            return Response.ok(dto).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @PUT
    @Path("/{requestId}/accept")
    public Response acceptRideRequest(@PathParam("requestId") Long requestId) {

        String driverId = (String) principal.getClaim("id");

        try {
            this.manageRideRequests.acceptRideRequest(driverId, requestId);
        } catch (AcceptInactiveRequestException e) {
            return Response.notModified().entity(AcceptInactiveRequestException.EXCEPTIONMESSAGE).build();
        } catch (NoMoreSeatsAvailableException e) {
            return Response.notModified().entity(NoMoreSeatsAvailableException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("/{requestId}/reject")
    public Response rejectRideRequest(@PathParam("requestId") Long requestId) {

        String driverId = (String) principal.getClaim("id");

        try {
            this.manageRideRequests.rejectRideRequest(driverId, requestId);
        } catch (RejectInactiveRequestException e) {
            return Response.notModified().entity(RejectInactiveRequestException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("/{requestId}")
    public Response modifyRideRequest(@PathParam("requestId") Long requestId, @Valid RideRequestDTO dto) {
        try {
            String passengerId = (String) principal.getClaim("id");

            RideRequest rideRequest = RideRequestDTO.mapDTOToRideRequest(dto);

            if (rideRequest != null) {
                rideRequest.setId(requestId);
                this.manageRideRequests.modifyRideRequest(passengerId, rideRequest);
                return Response.ok().build();
            }
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @DELETE
    @Path("/{requestId}")
    public Response revokeRideRequest(@PathParam("requestId") Long requestId) {

        String passengerId = (String) principal.getClaim("id");

        boolean deleted = false;

        try {
            deleted = this.manageRideRequests.revokeRideRequest(passengerId, requestId);
        } catch (RideRequestAlreadyInactiveException e) {
            return Response.notModified().entity(RideRequestAlreadyInactiveException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        if (deleted) {
            return Response.ok().build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }
}
