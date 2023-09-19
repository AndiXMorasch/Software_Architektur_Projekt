package de.hsos.studcar.notificationmanagement.gateway;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.studcar.notificationmanagement.entity.Notification;
import de.hsos.studcar.notificationmanagement.entity.NotificationCatalog;

@Transactional(TxType.MANDATORY)
@ApplicationScoped
public class NotificationRepository implements NotificationCatalog {

    @Inject
    EntityManager em;

    @Override
    public Collection<Notification> selectNotificationsByUser(String receiverId) {
        TypedQuery<NotificationEntity> findAll = em.createQuery(
                "SELECT n FROM NotificationEntity n WHERE n.receiverId =: receiverId ",
                NotificationEntity.class);
        findAll.setParameter("receiverId", receiverId);
        List<NotificationEntity> entities = findAll.getResultList();
        return entities.stream()
                .map(NotificationEntity::getNotificationFromEntity)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .toList();
    }

    @Override
    public Notification selectNotificationById(String receiver, Long notificationId) {
        NotificationEntity entity = em.find(NotificationEntity.class, notificationId);

        if (entity == null) {
            return null;
        }

        return NotificationEntity.getNotificationFromEntity(entity);
    }

    @Override
    public Long createNotification(Notification notification) {
        NotificationEntity entity = NotificationEntity.getEntityFromNotification(notification);

        this.em.persist(entity);
        return entity.getId();
    }

    @Override
    public boolean setNotificationAsRead(String receiverId, Long notificationId) {
        NotificationEntity entity = em.find(NotificationEntity.class, notificationId);

        if (entity == null) {
            return false;
        }

        entity.setRead(true);
        em.merge(entity);
        return true;
    }

}
