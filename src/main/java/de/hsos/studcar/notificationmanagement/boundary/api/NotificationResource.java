package de.hsos.studcar.notificationmanagement.boundary.api;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.studcar.notificationmanagement.entity.Notification;
import de.hsos.studcar.notificationmanagement.entity.NotificationCatalog;

import javax.transaction.Transactional.TxType;

@RequestScoped
@Transactional(TxType.REQUIRES_NEW)
@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private final String NOTFOUNDMESSAGE = "There is no notification with this notificationId.";

    @Inject
    NotificationCatalog notificationCatalog;

    @Inject
    JsonWebToken principal;

    @GET
    public Response getAllUserNotifications() {
        String userId = (String) principal.getClaim("id");
        Collection<Notification> notifications = this.notificationCatalog.selectNotificationsByUser(userId);
        Collection<NotificationDTO> dtos = notifications.stream()
                .map(NotificationDTO::mapNotificationToDTO)
                .toList();
        return Response.ok()
                .entity(dtos)
                .build();
    }

    @GET
    @Path("/{notificationId}")
    public Response getNotificationById(@PathParam("notificationId") Long notificationId) {
        String userId = (String) principal.getClaim("id");
        Notification notification = this.notificationCatalog.selectNotificationById(userId, notificationId);
        NotificationDTO dto = NotificationDTO.mapNotificationToDTO(notification);

        if (notification != null) {
            return Response.ok(dto).build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }

    @PUT
    @Path("/{notificationId}")
    public Response setNotificationAsRead(@PathParam("notificationId") Long notificationId) {
        String userId = (String) principal.getClaim("id");
        boolean successful = this.notificationCatalog.setNotificationAsRead(userId, notificationId);
        if (successful) {
            return Response.ok().build();
        }

        return Response.status(Status.NOT_FOUND).entity(NOTFOUNDMESSAGE).build();
    }
}
