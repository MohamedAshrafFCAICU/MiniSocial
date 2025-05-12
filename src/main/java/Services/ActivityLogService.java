package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import CustomizedExceptions.UnAuthorizedException;
import DTOs.EventToPassDto;
import DTOs.LogToReturnDto;
import Entities.ActivityLog;
import Entities.User;
import Enums.Role;
import RepositoriesContract.IActivityLogRepository;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.IUserRepository;
import ServicesContract.IActivityLogService;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.*;

import java.util.ArrayList;
import java.util.List;


@Stateless
public class ActivityLogService implements IActivityLogService {

    @Resource(mappedName = "java:/jms/queue/ActivityLogQueue")
    private Queue logQueue;

    @Inject
    @JMSConnectionFactory("java:/ConnectionFactory")
    private JMSContext context;

    @EJB
    private IActivityLogRepository logRepository;

    @EJB
    private IUserRepository userRepository;

    @EJB
    private IAuthenticationRepository authenticationRepository;


    @Override
    public void log(EventToPassDto logEvent)
    {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUser(userRepository.getById(logEvent.getUserId()));
        activityLog.setActivity(logEvent.getContent());
        activityLog.setEntityType(logEvent.getEntityType());
        activityLog.setEntityId(logEvent.getEntityId());

        logRepository.add(activityLog);


        JMSProducer producer = context.createProducer();
        ObjectMessage message = context.createObjectMessage(logEvent);
        producer.send(logQueue, message);

    }

    @Override
    public List<LogToReturnDto> getUserLogs(String token,int userId ,  int numberOfLogs)
    {
       try {
           User user = _AuthenticationService.authenticate(token, authenticationRepository);

           if(user.getRole() != Role.ADMIN && user.getId() != userId)
               throw new UnAuthorizedException("You are not authorized to access these logs");

           List<ActivityLog> activityLogs =  logRepository.getActivityLogsByUserId(userId, numberOfLogs);

           List<LogToReturnDto> logsDto = new ArrayList<>();
           if(activityLogs == null)
               return logsDto;


           for (ActivityLog activityLog : activityLogs) {
               LogToReturnDto logDto = new LogToReturnDto();
               logDto.setLogId(activityLog.getId());
               logDto.setUserId(activityLog.getUser().getId());
               logDto.setLogContent(activityLog.getActivity());

               logsDto.add(logDto);
           }

           return logsDto;
       }
       catch (InternalServerException e)
       {
           throw e;

       }


    }

    @Override
    public List<LogToReturnDto> getRecentLogs(String token , int numberOfLogs)
    {

        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);

            if(user.getRole() != Role.ADMIN)
                throw new UnAuthorizedException("You are not authorized to access these logs");

            List<ActivityLog> activityLogs =  logRepository.getRecentActivityLogs(numberOfLogs);

            List<LogToReturnDto> logsDto = new ArrayList<>();
            if(activityLogs == null)
                return logsDto;


            for (ActivityLog activityLog : activityLogs) {
                LogToReturnDto logDto = new LogToReturnDto();
                logDto.setLogId(activityLog.getId());
                logDto.setUserId(activityLog.getUser().getId());
                logDto.setLogContent(activityLog.getActivity());

                logsDto.add(logDto);
            }

            return logsDto;
        }
        catch (InternalServerException e)
        {
            throw e;
        }

    }
}
