package APIs;

import DTOs.LogToReturnDto;
import ServicesContract.IActivityLogService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/logs")
public class ActivityLogResource
{
    @EJB
    private IActivityLogService activityLogService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LogToReturnDto> getUserLogs(@HeaderParam("Authorization") String token , @QueryParam("userId") int userId,  @QueryParam("limit")  int limit)
    {
        return activityLogService.getUserLogs(token,userId,limit);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LogToReturnDto> getLogs(@HeaderParam("Authorization") String token ,  @QueryParam("limit") int limit)
    {
        return activityLogService.getRecentLogs(token,limit);
    }

}
