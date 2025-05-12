package ServicesContract;

import DTOs.EventToPassDto;
import DTOs.LogToReturnDto;

import java.util.List;

public interface IActivityLogService
{
    public void log(EventToPassDto logEvent);

    public List<LogToReturnDto> getUserLogs(String token , int userId, int numberOfLogs);

    public List<LogToReturnDto> getRecentLogs(String token , int numberOfLogs);
}
