package ServicesContract;

import DTOs.EventToPassDto;
import DTOs.NotificationToReturnDto;

public interface INotificationService
{
    public void sendNotification(EventToPassDto notificationEventDto);

    public NotificationToReturnDto openNotification(String token , int notificationId);
}
