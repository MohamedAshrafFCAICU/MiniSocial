package MessageDrivenBeans;

import DTOs.EventToPassDto;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;


@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "destinationLookup",
                        propertyValue = "java:/jms/queue/NotificationQueue"
                ),
                @ActivationConfigProperty(
                        propertyName = "destinationType",
                        propertyValue = "jakarta.jms.Queue"
                ),
                @ActivationConfigProperty(
                        propertyName = "acknowledgeMode",
                        propertyValue = "Auto-acknowledge"
                )
        }
)
public class NotificationConsumer implements MessageListener {
    @Override
    public void onMessage(Message message)
    {
        try
        {
            if(!(message instanceof ObjectMessage))
                return;
            ObjectMessage objectMessage = (ObjectMessage)message;
            Object object = objectMessage.getObject();
            if(!(object instanceof EventToPassDto))
                return;
            EventToPassDto notificationEventToPassDto = (EventToPassDto) object;

            System.out.println("Notification");
            System.out.println("User Id: "+ notificationEventToPassDto.getUserId());
            System.out.println("Event Type: "+ notificationEventToPassDto.getEventType());
            System.out.println("Content: " + notificationEventToPassDto.getContent());
            System.out.println("-------------------------------------------------------------");

        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }


    }
}
