package de.hsos.studcar.offermanagement.boundary.api;

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

import de.hsos.studcar.offermanagement.boundary.api.dto.NewOfferDTO;
import de.hsos.studcar.offermanagement.boundary.api.dto.NewRideRequestDTO;
import de.hsos.studcar.offermanagement.boundary.api.dto.OfferDTO;
import de.hsos.studcar.offermanagement.control.ManageOffers;
import de.hsos.studcar.offermanagement.entity.Offer;
import de.hsos.studcar.offermanagement.entity.RideRequest;
import de.hsos.studcar.offermanagement.entity.exceptions.AddRequestToInactiveOfferException;

@RequestScoped
@Transactional(TxType.REQUIRES_NEW)
@Path("/offers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OfferResource {

    private final String NOTFOUNDMESSAGE = "There is no offer with this offerId.";

    @Inject
    ManageOffers manageOffers;

    @Inject
    JsonWebToken principal;

    @GET
    public Response getAllOffers(@QueryParam("sorting") String sorting,
            @QueryParam("driveDate") String driveDate, @QueryParam("destinationCampus") String destinationCampus,
            @QueryParam("seats") Integer seats) {

        Collection<Offer> offers = this.manageOffers.selectAllOffers(sorting, driveDate,
                destinationCampus, seats);
        Collection<OfferDTO> dtos = offers.stream().map(OfferDTO::mapOfferToDTO).toList();
        return Response.ok()
                .entity(dtos)
                .build();
    }

    @POST
    public Response createNewOffer(@Valid NewOfferDTO dto) {

        String driverId = (String) principal.getClaim("id");

        Long offerId = this.manageOffers.createOffer(driverId, NewOfferDTO.mapDTOToOffer(dto));
        if (offerId != null) {
            return Response.ok(offerId).build();
        }

        // Internal Server Error wenn eine neue Offer nicht angelegt werden kann,
        // bis hierhin darf das Progamm im Optimalfall nicht kommen
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{offerId}")
    public Response getOfferDetails(@PathParam("offerId") Long offerId) {
        Offer offer = this.manageOffers.selectOffer(offerId);

        if (offer != null) {
            OfferDTO dto = OfferDTO.mapOfferToDTO(offer);
            return Response.ok(dto).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @PUT
    @Path("/{offerId}")
    public Response modifyOffer(@Valid OfferDTO dto) {
        try {
            String driverId = (String) principal.getClaim("id");

            Offer offer = OfferDTO.mapDTOToOffer(dto);
            boolean modified = this.manageOffers.modifyOffer(driverId, offer);

            if (modified) {
                return Response.ok().build();
            }
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @POST
    @Path("/{offerId}")
    public Response addRideRequestToOffer(@PathParam("offerId") Long offerId, @Valid NewRideRequestDTO dto) {

        Offer offer = this.manageOffers.selectOffer(offerId);
        boolean wasAdded = false;
        String passengerId = (String) principal.getClaim("id");

        try {
            if (offer != null) {
                RideRequest rideRequest = NewRideRequestDTO.mapDTOToRideRequest(dto);
                rideRequest.setPassengerId(passengerId);
                wasAdded = this.manageOffers.addRideRequestToOffer(rideRequest, offerId);
            }
        } catch (AddRequestToInactiveOfferException e) {
            return Response.notModified().entity(AddRequestToInactiveOfferException.EXCEPTIONMESSAGE).build();
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        if (wasAdded) {
            return Response.ok().build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @DELETE
    @Path("/{offerId}")
    public Response revokeOffer(@PathParam("offerId") Long offerId) {
        try {
            String driverId = (String) principal.getClaim("id");

            boolean deleted = this.manageOffers.revokeOffer(driverId, offerId);

            if (deleted) {
                return Response.ok().build();
            }
        } catch (SecurityException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

}
