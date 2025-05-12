package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import DTOs.NotificationEventToPassDto;
import DTOs.NotificationToReturnDto;
import Entities.Notification;
import Entities.User;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.INotificationRepository;
import RepositoriesContract.IUserRepository;
import ServicesContract.INotificationService;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.*;


@Stateless
public class NotificationService implements INotificationService
{
    @Resource(mappedName = "java:/jms/queue/NotificationQueue")
    private  Queue notificationQueue;

    @Inject
    @JMSConnectionFactory("java:/ConnectionFactory")
    private JMSContext context;

    @EJB
    private INotificationRepository notificationRepository;

    @EJB
    private IUserRepository userRepository;

    @EJB
    private IAuthenticationRepository authenticationRepository;

    @Override
    public void sendNotification(NotificationEventToPassDto notificationEventDto)
    {
        Notification notification = new Notification();
        notification.setEventType(notificationEventDto.getEventType());
        notification.setContent(notificationEventDto.getContent());
        notification.setRead(notificationEventDto.getRead());
        notification.setUser(userRepository.getById(notificationEventDto.getUserId()));
        notification.setEntityType(notificationEventDto.getEntityType());
        notification.setEntityId(notificationEventDto.getEntityId());

        notificationRepository.add(notification);


        JMSProducer producer = context.createProducer();
        ObjectMessage message = context.createObjectMessage(notificationEventDto);
        producer.send(notificationQueue, message);
    }

    @Override
    public NotificationToReturnDto openNotification(String token, int notificationId)
    {

        try
        {
            User user = _AuthenticationService.authenticate(token , authenticationRepository);

            Notification notification = notificationRepository.getById(notificationId);

            if(notification == null)
                throw new ClientException("There is no notification with id " + notificationId);

            if(user.getId() != notification.getUser().getId())
                throw new ClientException("You are not authorized to access this notification.");

            NotificationToReturnDto notificationToReturnDto = new NotificationToReturnDto();
            notificationToReturnDto.setNotificationId(notification.getId());
            notificationToReturnDto.setEventType(notification.getEventType());
            notificationToReturnDto.setContent(notification.getContent());
            notificationToReturnDto.setEventType(notification.getEventType());



            notification.setRead(true);
            notificationRepository.update(notification);

            return notificationToReturnDto;
        }
        catch (InternalServerException e)
        {
            throw new InternalServerException("Internal Server Exception.");
        }


    }

}
