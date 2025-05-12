package APIs;

import DTOs.NotificationToReturnDto;
import ServicesContract.INotificationService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


@Path("/notifications")
public class NotificationResource
{
    @EJB
    private INotificationService notificationService;

    @GET
    @Path("/{notificationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public NotificationToReturnDto openNotification(@HeaderParam("Authorization") String token , @PathParam("notificationId") int notificationId)
    {
        return notificationService.openNotification(token, notificationId);
    }
}
