package APIs;

import DTOs.UserToLoginDto;
import DTOs.UserToRegisterDto;
import DTOs.UserToReturnDto;
import DTOs.UserToUpdateProfileDto;
import ServicesContract.IIdentityService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UserResource
{
    @Inject
    private IIdentityService identityService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerUser(@Valid UserToRegisterDto userDto)
    {
        return identityService.register(userDto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String loginUser(@Valid UserToLoginDto userDto)
    {
        return identityService.login(userDto);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@HeaderParam("Authorization") String token)
    {
        return identityService.logout(token);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateUser(@HeaderParam("Authorization") String token , UserToUpdateProfileDto userDto)
    {
        return identityService.updateProfile(token, userDto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserToReturnDto getUserByEmail(@HeaderParam("Authorization") String token ,@QueryParam("email") String email )
    {
        return identityService.getUserByEmail(token, email);
    }

    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@HeaderParam("Authorization") String token ,@PathParam("userId")int userId )
    {
       return identityService.deleteUser(token, userId);
    }



}
