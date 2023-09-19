package de.hsos.studcar.notificationmanagement.boundary.api;

import de.hsos.studcar.notificationmanagement.entity.Notification;

public class NotificationDTO {

    public Long id;

    public String receiverId;

    public String headline;

    public String message;

    public String date;

    public boolean read;

    public static NotificationDTO mapNotificationToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.id = notification.getId();
        dto.receiverId = notification.getReceiverId();
        dto.headline = notification.getHeadline();
        dto.message = notification.getMessage();
        dto.date = notification.getDate();
        dto.read = notification.wasRead();
        return dto;
    }
}
