package APIs;

import DTOs.PostToCreateDto;
import DTOs.PostToUpdateDto;
import Entities.Post;
import ServicesContract.IPostService;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/posts")
public class PostResource {

    @EJB
    private IPostService postService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPost(@HeaderParam("Authorization") String token, @Valid PostToCreateDto postDto ) {
        return postService.createPost(token, postDto);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePost(@HeaderParam("Authorization") String token, @Valid PostToUpdateDto postDto ) {
        return postService.updatePost(token, postDto);
    }

    @DELETE
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePost(@HeaderParam("Authorization") String token, @PathParam("postId") int postId) {
        return postService.deletePost(token, postId);
    }


    @GET
    @Path("/feed")
    @Produces(MediaType.APPLICATION_JSON)
    public java.util.List<DTOs.PostToReturnDto> getUserFeed(@HeaderParam("Authorization") String token) {
        return postService.getUserFeed(token);
    }


}
