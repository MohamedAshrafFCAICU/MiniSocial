package ServicesContract;

import DTOs.NotificationEventToPassDto;
import DTOs.NotificationToReturnDto;

public interface INotificationService
{
    public void sendNotification(NotificationEventToPassDto notificationEventDto);

    public NotificationToReturnDto openNotification(String token , int notificationId);
}
