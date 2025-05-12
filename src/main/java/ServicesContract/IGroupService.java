package ServicesContract;

import DTOs.GroupResponseToCreateDto;
import DTOs.GroupToCreateDto;
import DTOs.PostInGroupToCreateDto;

public interface IGroupService
{
    public String createGroup(String token , GroupToCreateDto groupDto);

    public String joinGroup(String token , int groupId);

    public String leaveGroup(String token , int groupId);

    public String respondGroupRequest(String token , GroupResponseToCreateDto groupDto);

    public String createPostInGroup(String token , PostInGroupToCreateDto postInGroupDto);

    public String makeAdminInGroup(String token , int groupId , int memberId);

    public String removeMemberFromGroup(String token , int groupId , int memberId);

    public String deleteGroup(String token , int groupId);

    public String deletePostInGroup(String token , int groupId , int postId);
}
