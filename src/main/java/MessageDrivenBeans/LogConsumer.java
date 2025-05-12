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
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/ActivityLogQueue"),
                @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
        }
)
public class LogConsumer implements MessageListener
{


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
            EventToPassDto logEventDto = (EventToPassDto) object;

            System.out.println("LOG:");
            System.out.println("User Id: "+ logEventDto.getUserId());
            System.out.println("Event Type: "+ logEventDto.getEventType());
            System.out.println("Content: " + logEventDto.getContent());
            System.out.println("-------------------------------------------------------------");

        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
    }
}
