package APIs;

import DTOs.FriendRequestToReceiveDto;
import DTOs.FriendToReturnDto;
import DTOs.FriendToSuggestDto;
import ServicesContract.IConnectionService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/connections")
public class ConnectionResource
{
    @EJB
    private IConnectionService connectionService;

    @POST
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public String requestUser(@HeaderParam("Authorization") String token , @QueryParam("receiverId") int receiverId)
    {
        return connectionService.requestUser(token, receiverId);
    }

    @GET
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FriendRequestToReceiveDto> getFriendRequestsReceived(@HeaderParam("Authorization") String token)
    {
        return connectionService.getFriendRequests(token);
    }


    @PUT
    @Path("/requests/{requestId}/accept")
    @Produces(MediaType.APPLICATION_JSON)
    public String acceptFriendRequest(@HeaderParam("Authorization") String token ,@PathParam("requestId") int friendRequestId)
    {
        return connectionService.acceptFriendRequest(token, friendRequestId);
    }

    @PUT
    @Path("requests/{requestId}/reject")
    @Produces(MediaType.APPLICATION_JSON)
    public String rejectFriendRequest(@HeaderParam("Authorization") String token ,@PathParam("requestId") int friendRequestId)
    {
        return connectionService.rejectFriendRequest(token, friendRequestId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FriendToReturnDto> getConnections(@HeaderParam("Authorization") String token)
    {
        return connectionService.getFriends(token);
    }

    @GET
    @Path("/suggestions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FriendToSuggestDto> getSuggestions(@HeaderParam("Authorization") String token)
    {
        return connectionService.getFriendSuggestions(token);
    }
}
