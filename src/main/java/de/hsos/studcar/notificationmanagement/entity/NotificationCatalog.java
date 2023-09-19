package de.hsos.studcar.notificationmanagement.entity;

import java.util.Collection;

public interface NotificationCatalog {

    Collection<Notification> selectNotificationsByUser(String receiverId);

    Notification selectNotificationById(String receiverId, Long notificationId);

    Long createNotification(Notification notification);

    boolean setNotificationAsRead(String receiverId, Long notificationId);
}
