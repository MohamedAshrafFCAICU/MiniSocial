package APIs;

import DTOs.LikeForCommentToCreateDto;
import DTOs.LikeForPostToCreateDto;
import ServicesContract.ILikeService;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/likes")
public class LikeResource {

    @EJB
    private ILikeService likeService;

    // Add like to a post
    @POST
    @Path("/posts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addLikeForPost(@HeaderParam("Authorization") String token,
                                 @Valid LikeForPostToCreateDto likeForPostDto) {
        return likeService.addLikeForPost(token, likeForPostDto);
    }

    // Remove like from a post
    @DELETE
    @Path("/posts/{likeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeLikeForPost(@HeaderParam("Authorization") String token,
                                    @PathParam("likeId") int likeId) {
        return likeService.removeLikeForPost(token, likeId);
    }

    // Add like to a comment
    @POST
    @Path("/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addLikeForComment(@HeaderParam("Authorization") String token,
                                    @Valid LikeForCommentToCreateDto likeForCommentDto) {
        return likeService.addLikeForComment(token, likeForCommentDto);
    }

    // Remove like from a comment
    @DELETE
    @Path("/comments/{likeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeLikeForComment(@HeaderParam("Authorization") String token,
                                       @PathParam("likeId") int likeId) {
        return likeService.removeLikeForComment(token, likeId);
    }
}
