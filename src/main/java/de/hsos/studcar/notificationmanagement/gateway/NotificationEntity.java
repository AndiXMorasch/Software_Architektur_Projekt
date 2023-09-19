package de.hsos.studcar.notificationmanagement.gateway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import de.hsos.studcar.notificationmanagement.entity.Notification;

@Entity
public class NotificationEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private String receiverId;

    private String headline;

    private String message;

    private String date;

    private boolean read;

    public static Notification getNotificationFromEntity(NotificationEntity entity) {
        return new Notification(entity.getId(), entity.getReceiverId(), entity.getHeadline(), entity.getMessage(),
                entity.getDate(), entity.wasRead());
    }

    public static NotificationEntity getEntityFromNotification(Notification notification) {
        NotificationEntity entity = new NotificationEntity();

        entity.setReceiverId(notification.getReceiverId());
        entity.setHeadline(notification.getHeadline());
        entity.setMessage(notification.getMessage());
        entity.setDate(notification.getDate());

        return entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean wasRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
