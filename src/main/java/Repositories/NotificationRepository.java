package Repositories;

import Entities.Notification;
import RepositoriesContract.INotificationRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class NotificationRepository extends _GenericRepository<Notification> implements INotificationRepository
{
    public NotificationRepository()
    {
        super(Notification.class);
    }


    @Override
    public List<Notification> getNotificationsByUserId(int userId)
    {
        TypedQuery<Notification> query = entityManager.createQuery("SELECT n FROM Notification n WHERE n.user.id = :userId", Notification.class);
        query.setParameter("userId", userId);

        List<Notification> notifications = query.getResultList();

        return notifications == null ? null: notifications;
    }
}
