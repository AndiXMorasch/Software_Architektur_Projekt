package de.hsos.studcar.requestmanagement.boundary.api;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.Valid;
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

import de.hsos.studcar.requestmanagement.boundary.api.dto.DriveOfferDTO;
import de.hsos.studcar.requestmanagement.control.ManageDriveOffers;
import de.hsos.studcar.requestmanagement.entity.DriveOffer;
import de.hsos.studcar.requestmanagement.entity.exceptions.AcceptInactiveOfferException;
import de.hsos.studcar.requestmanagement.entity.exceptions.DriveOfferAlreadyInactiveException;
import de.hsos.studcar.requestmanagement.entity.exceptions.RejectInactiveOfferException;

@RequestScoped
@Transactional(TxType.REQUIRES_NEW)
@Path("/requests/driveoffers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DriveOfferResource {

    private final String NOTFOUNDMESSAGE = "There is no DriveOffer with this offerId.";

    @Inject
    ManageDriveOffers manageDriveOffers;

    @Inject
    JsonWebToken principal;

    @GET
    public Response getAllDriveOffers() {

        String driverId = (String) principal.getClaim("id");

        Collection<DriveOffer> offers = this.manageDriveOffers.selectAllDriveOffers(driverId);
        Collection<DriveOfferDTO> dtos = offers.stream().map(DriveOfferDTO::mapDriveOfferToDTO).toList();
        return Response.ok()
                .entity(dtos)
                .build();
    }

    @GET
    @Path("/{offerId}")
    public Response getDriveOfferDetails(@PathParam("offerId") Long offerId) {

        String driverId = (String) principal.getClaim("id");

        DriveOffer driveOffer = this.manageDriveOffers.selectDriveOffer(driverId, offerId);

        if (driveOffer != null) {
            DriveOfferDTO dto = DriveOfferDTO.mapDriveOfferToDTO(driveOffer);
            return Response.ok(dto).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @PUT
    @Path("/{offerId}/accept")
    public Response acceptDriveOffer(@PathParam("offerId") Long offerId) {

        String passengerId = (String) principal.getClaim("id");

        try {
            this.manageDriveOffers.acceptDriveOffer(passengerId, offerId);
        } catch (AcceptInactiveOfferException e) {
            return Response.notModified().entity(AcceptInactiveOfferException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("/{offerId}/reject")
    public Response rejectDriveOffer(@PathParam("offerId") Long offerId) {

        String passengerId = (String) principal.getClaim("id");

        try {
            this.manageDriveOffers.rejectDriveOffer(passengerId, offerId);
        } catch (RejectInactiveOfferException e) {
            return Response.notModified().entity(RejectInactiveOfferException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("/{offerId}")
    public Response modifyDriveOffer(@Valid DriveOfferDTO dto) {
        try {
            String driverId = (String) principal.getClaim("id");

            DriveOffer driveOffer = DriveOfferDTO.mapDTOToDriveOffer(dto);

            if (driveOffer != null) {
                this.manageDriveOffers.modifyDriveOffer(driverId, driveOffer);
                return Response.ok().build();
            }
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @DELETE
    @Path("/{offerId}")
    public Response revokeDriveOffer(@PathParam("offerId") Long offerId) {

        String driverId = (String) principal.getClaim("id");

        boolean deleted = false;

        try {
            deleted = this.manageDriveOffers.revokeDriveOffer(driverId, offerId);
        } catch (DriveOfferAlreadyInactiveException e) {
            return Response.notModified().entity(DriveOfferAlreadyInactiveException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        if (deleted) {
            return Response.ok().build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

}
