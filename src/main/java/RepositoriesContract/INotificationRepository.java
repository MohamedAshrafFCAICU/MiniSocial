package RepositoriesContract;

import Entities.Notification;

import java.util.List;

public interface INotificationRepository extends _IGenericRepository<Notification>
{
    public List<Notification> getNotificationsByUserId(int userId);
}
