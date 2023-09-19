package de.hsos.studcar.notificationmanagement.entity.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;

import de.hsos.studcar.notificationmanagement.entity.Notification;
import de.hsos.studcar.notificationmanagement.entity.NotificationCatalog;
import de.hsos.studcar.shared.events.DriveOfferAcceptedEvent;
import de.hsos.studcar.shared.events.DriveOfferChangedEvent;
import de.hsos.studcar.shared.events.DriveOfferRejectedEvent;
import de.hsos.studcar.shared.events.NewDriveOfferCreatedEvent;
import de.hsos.studcar.shared.events.NewRideRequestCreatedEvent;
import de.hsos.studcar.shared.events.OfferChangedEvent;
import de.hsos.studcar.shared.events.OfferRevokedEvent;
import de.hsos.studcar.shared.events.RequestChangedEvent;
import de.hsos.studcar.shared.events.RequestRevokedEvent;
import de.hsos.studcar.shared.events.RideRequestAcceptedEvent;
import de.hsos.studcar.shared.events.RideRequestChangedEvent;
import de.hsos.studcar.shared.events.RideRequestRejectedEvent;
import de.hsos.studcar.shared.users.UserService;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class NotificationService {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    @Inject
    NotificationCatalog notificationCatalog;

    @Inject
    UserService userService;

    @Inject
    Mailer mailer;

    public void onOfferChanged(@Observes OfferChangedEvent event) {
        String headline = "Ein Mitnahmeangebot hat sich geändert";
        String message;

        for (Map.Entry<Long, String> entry : event.requestIdsToPassengerIds.entrySet()) {
            message = "Ein Mitnahmeangebot wo Sie sich als Passagier beworben habe wurde vom " +
                    "Fahrer geändert. Bitte überprüfen Sie die Änderungen und reagieren Sie " +
                    "falls notwendig ";
            Notification notification = notificationBuilder(entry.getValue(), headline, message);
            this.notificationCatalog.createNotification(notification);
            sendMailToUser(notification);
        }
    }

    public void onOfferRevoked(@Observes OfferRevokedEvent event) {
        String headline = "Ein Mitnahmeangebot wurde zurückgezogen";
        String message;

        for (Map.Entry<Long, String> entry : event.requestIdsToPassengerIds.entrySet()) {
            message = "Ein Mitnahmeangebot wo Sie sich als Passagier beworben haben wurde vom Fahrer " +
                    "zurückgezogen. Aus diesem Grund wird die Fahrt nicht stattfinden.";
            Notification notification = notificationBuilder(entry.getValue(), headline, message);
            this.notificationCatalog.createNotification(notification);
            sendMailToUser(notification);
        }
    }

    public void onNewRideRequestCreated(@Observes NewRideRequestCreatedEvent event) {
        String headline = "Eine neue Mitnahmeanfrage ist eingegangen";
        String message = "Eine neue Mitnahmeanfrage ist für eine ihrer Mitfahrgelegenheiten eingegangen. " +
                "Bitte akzeptieren oder lehnen Sie die Anfrage ab.";
        Notification notification = notificationBuilder(event.driverId, headline, message);
        this.notificationCatalog.createNotification(notification);
        sendMailToUser(notification);
    }

    public void onRideRequestChanged(@Observes RideRequestChangedEvent event) {
        // Hier passiert nichts...
    }

    public void onRideRequestAccepted(@Observes RideRequestAcceptedEvent event) {
        String headline = "Ihre Mitnahmeanfrage wurde akzeptiert";
        String message = "Ihre Mitnahmeanfrage wurde vom Fahrer akzeptiert. Wir wünschen Ihnen eine gute Fahrt.";
        Notification notification = notificationBuilder(event.passengerId, headline, message);
        this.notificationCatalog.createNotification(notification);
        sendMailToUser(notification);
    }

    public void onRideRequestRejected(@Observes RideRequestRejectedEvent event) {
        String headline = "Ihre Mitnahemeanfrage wurde abgelehnt";
        String message = "Bedauerlicherweise wurde ihre Mitnahmeanfrage vom Fahrer abgelehnt.";
        Notification notification = notificationBuilder(event.passengerId, headline, message);
        this.notificationCatalog.createNotification(notification);
        sendMailToUser(notification);
    }

    public void onRequestChanged(@Observes RequestChangedEvent event) {
        String headline = "Eine Mitnahmeanfrage wurde geändert";
        String message;

        for (Map.Entry<Long, String> entry : event.offerIdsToDriverIds.entrySet()) {
            message = "Die Mitnahmeanfrage wo Sie sich als Fahrer beworben haben wurde " +
                    "vom Passagier geändert. Bitte überprüfen Sie die Änderungen und reagieren Sie " +
                    "falls notwendig.";
            Notification notification = notificationBuilder(entry.getValue(), headline, message);
            this.notificationCatalog.createNotification(notification);
            sendMailToUser(notification);
        }
    }

    public void onRequestRevokedEvent(@Observes RequestRevokedEvent event) {
        String headline = "Eine Mitnahmeanfrage wurde zurückgezogen";
        String message;

        for (Map.Entry<Long, String> entry : event.offerIdsToDriverIds.entrySet()) {
            message = "Die Mitnahmeanfrage wo Sie sich als Fahrer beworben haben wurde " +
                    "vom Passagier zurückgezogen. Es besteht daher kein Grund die Person mitzunehmen.";
            Notification notification = notificationBuilder(entry.getValue(), headline, message);
            this.notificationCatalog.createNotification(notification);
            sendMailToUser(notification);
        }
    }

    public void onNewDriveOfferCreated(@Observes NewDriveOfferCreatedEvent event) {
        String headline = "Eine neues Mitnahmeangebot ist eingegangen";
        String message = "Ein neues Mitnahmeangebot ist für eine ihrer Mitnahmeanfragen eingegangen. " +
                "Bitte akzeptieren oder lehnen Sie das Angebot ab.";
        Notification notification = notificationBuilder(event.passengerId, headline, message);
        this.notificationCatalog.createNotification(notification);
        sendMailToUser(notification);
    }

    public void onDriveOfferChanged(@Observes DriveOfferChangedEvent event) {
        // Hier passiert nichts...
    }

    public void onDriveOfferAccepted(@Observes DriveOfferAcceptedEvent event) {
        String headline = "Ihr Mitnahmeangebot wurde akzeptiert";
        String message = "Ihr Mitnahmeangebot wurde vom Passagier akzeptiert. " +
                "Bitte planen Sie ein die Person mitzunehmen. Wir wünschen Ihnen eine gute Fahrt.";
        Notification notification = notificationBuilder(event.driverId, headline, message);
        this.notificationCatalog.createNotification(notification);
        sendMailToUser(notification);
    }

    public void onDriveOfferRejected(@Observes DriveOfferRejectedEvent event) {
        String headline = "Ihr Mitnahmeangebot wurde abgelehnt";
        String message = "Ihr Mitnahmeangebot wurde vom Passagier abgelehnt. " +
                "Es besteht daher kein Grund die Person mitzunehmen.";
        Notification notification = notificationBuilder(event.driverId, headline, message);
        this.notificationCatalog.createNotification(notification);
        sendMailToUser(notification);
    }

    private Notification notificationBuilder(String receiverId, String headline, String message) {
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setHeadline(headline);
        notification.setMessage(message);
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        notification.setDate(date);
        return notification;
    }

    @Blocking
    // Hinweis: Es werden keine echten Mails im Quarkus Dev Mode versendet!
    private void sendMailToUser(Notification notification) {
        String email = this.userService.getUser(notification.getReceiverId()).email;
        mailer.send(Mail.withText(email, notification.getHeadline(),
                notification.getMessage()));
    }

}
