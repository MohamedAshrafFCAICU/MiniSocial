package APIs;

import DTOs.GroupResponseToCreateDto;
import DTOs.GroupToCreateDto;
import DTOs.PostInGroupToCreateDto;
import ServicesContract.IGroupService;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/groups")
public class GroupResource {
    @EJB
    private IGroupService groupService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPost(@HeaderParam("Authorization") String token, @Valid GroupToCreateDto groupDto) {
        return groupService.createGroup(token, groupDto);
    }


    @POST
    @Path("/requests/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String joinGroup(@HeaderParam("Authorization") String token, @PathParam("groupId") int groupId) {
        return groupService.joinGroup(token, groupId);
    }

    @DELETE
    @Path("/requests/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String leaveGroup(@HeaderParam("Authorization") String token, @PathParam("groupId") int groupId) {
        return groupService.leaveGroup(token, groupId);
    }

    @PUT
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String respondGroupRequest(@HeaderParam("Authorization") String token, @Valid GroupResponseToCreateDto groupDto) {
        return groupService.respondGroupRequest(token, groupDto);
    }

    @POST
    @Path("/posts")

    @Consumes(MediaType.APPLICATION_JSON)
    public String createPost(@HeaderParam("Authorization") String token, @Valid PostInGroupToCreateDto postInGroupDto) {
        return groupService.createPostInGroup(token, postInGroupDto);
    }

    @DELETE
    @Path("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePost(@HeaderParam("Authorization") String token, @QueryParam("groupId") int groupId, @QueryParam("postId") int postId)
    {
        return groupService.deletePostInGroup(token, groupId, postId);
    }

    @POST
    @Path("/members")
    @Produces(MediaType.APPLICATION_JSON)
    public String addGroupAdmin(@HeaderParam("Authorization") String token , @QueryParam("groupId") int groupId, @QueryParam("memberId") int memberId)
    {
        return groupService.makeAdminInGroup(token, groupId, memberId);
    }

    @DELETE
    @Path("/members")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeGroupMember(@HeaderParam("Authorization") String token , @QueryParam("groupId") int groupId, @QueryParam("memberId") int memberId)
    {
        return groupService.removeMemberFromGroup(token, groupId, memberId);
    }

    @DELETE
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeGroup(@HeaderParam("Authorization") String token , @PathParam("groupId") int groupId)
    {
        return groupService.deleteGroup(token, groupId);
    }


}