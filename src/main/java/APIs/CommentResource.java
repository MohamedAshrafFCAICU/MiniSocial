package APIs;

import DTOs.CommentToCreateDto;
import DTOs.CommentToUpdateDto;
import ServicesContract.ICommentService;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/comments")
public class CommentResource {

    @EJB
    private ICommentService commentService;

    // Add a comment or reply to a post
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addCommentToPost(@HeaderParam("Authorization") String token,
                                   @Valid CommentToCreateDto commentToCreateDto) {
        return commentService.addCommentToPost(token, commentToCreateDto);
    }

    // Update a comment
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateCommentToPost(@HeaderParam("Authorization") String token,
                                      @Valid CommentToUpdateDto commentToUpdateDto) {
        return commentService.updateCommentToPost(token, commentToUpdateDto);
    }

    // Delete a comment
    @DELETE
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCommentFromPost(@HeaderParam("Authorization") String token,
                                        @PathParam("commentId") int commentId) {
        return commentService.deleteCommentToPost(token, commentId);
    }
}
