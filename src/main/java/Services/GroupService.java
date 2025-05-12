package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import CustomizedExceptions.NotFoundException;
import CustomizedExceptions.UnAuthorizedException;
import DTOs.EventToPassDto;
import DTOs.GroupResponseToCreateDto;
import DTOs.GroupToCreateDto;
import DTOs.PostInGroupToCreateDto;
import Entities.*;
import Enums.RequestStatus;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.IGroupRepository;
import RepositoriesContract.IGroupRequestRepository;
import RepositoriesContract.IPostRepository;
import ServicesContract.IGroupService;
import ServicesContract.INotificationService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class GroupService implements IGroupService

{
    @EJB
    private IGroupRepository groupRepository;

    @EJB
    private IGroupRequestRepository groupRequestRepository;

    @EJB
    private IPostRepository postRepository;

    @EJB
    private INotificationService notificationService;

    @EJB
    private IAuthenticationRepository authenticationRepository;

    @Override
    public String createGroup(String token, GroupToCreateDto groupDto)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token , authenticationRepository);

            Group groupWithSameName = groupRepository.getGroupByName(groupDto.getGroupName());
            if(groupWithSameName != null)
                throw new ClientException("Group's name already exists");

            Group group = new Group();
            group.setGroupName(groupDto.getGroupName());
            group.setDescription(groupDto.getDescription());
            group.setPublic(groupDto.getIsPublic());
            group.setCreator(user);

            Set<User> admins = new HashSet<>();
            admins.add(user);
            group.setAdmins(admins);
            user.getAdminInGroups().add(group);
            groupRepository.add(group);


            return "Group created successfully";

        }
        catch (InternalServerException e)
        {
            throw e;
        }
    }

    @Override
    public String joinGroup(String token, int groupId)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(groupId);

            if (group == null)
                throw new NotFoundException("Group not found");

            if(groupRepository.isMemberInGroup(user.getId(), group.getId()))
                throw new ClientException("You are already member of this group");

            if(groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new ClientException("You are already an admin of this group.");

            GroupRequest groupRequestWithSameUser = groupRequestRepository.findRequestByUserIdAndGroupId(user.getId() , group.getId());
            if (groupRequestWithSameUser != null)
            {
                if(groupRequestWithSameUser.getStatus() == RequestStatus.PENDING)
                    throw new ClientException("You have already requested to join this group.");

                if (groupRequestWithSameUser.getStatus() == RequestStatus.ACCEPTED)
                    throw new ClientException("You are already a member of this group.");
            }

            if(group.getPublic())
                groupRepository.addMemberToGroup(user.getId(), group.getId());

            else
            {
                User creator = group.getCreator();
                GroupRequest groupRequest = new GroupRequest();
                groupRequest.setGroup(group);
                groupRequest.setReceiver(creator);
                groupRequest.setSender(user);
                groupRequestRepository.add(groupRequest);
            }



            EventToPassDto notificationEvent = new EventToPassDto();
            notificationEvent.setEventType("Join_Group");
            notificationEvent.setUserId(user.getId());
            notificationEvent.setContent("You have joined successfully to " + group.getGroupName() + " group");
            notificationEvent.setEntityType("Group_Request");
            if(group.getPublic())
                notificationEvent.setEntityId(0);

            else
                notificationEvent.setEntityId(groupRequestRepository.findRequestByUserIdAndGroupId(user.getId() , group.getId()).getId());

            notificationService.sendNotification(notificationEvent);

            if(group.getPublic())
                return "You are now member of this group";

            return "Group join request done successfully.";
        }
        catch (InternalServerException e)
        {
            throw e;
        }


    }

    @Override
    public String leaveGroup(String token, int groupId)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(groupId);

            if(group == null)
                throw new NotFoundException("Group not found");

            if(group.getCreator().getId() == user.getId())
                throw new ClientException("You Can not leave this group.");

            if(!groupRepository.isMemberInGroup(user.getId(), group.getId()) && !groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new ClientException("You are not a member of this group.");

            if(groupRepository.isAdminInGroup(user.getId(), group.getId()))
            {
                group.getAdmins().remove(user);
                user.getAdminInGroups().remove(group);
            }

            if(groupRepository.isMemberInGroup(user.getId(), group.getId()))
            {
                group.getMembers().remove(user);
                user.getMemberInGroups().remove(group);

            }
            groupRepository.update(group);

            GroupRequest groupRequest  = groupRequestRepository.findRequestByUserIdAndGroupId(user.getId(), group.getId());
            if(groupRequest != null)
                groupRequestRepository.delete(groupRequest.getId());


            EventToPassDto notificationEvent = new EventToPassDto();
            notificationEvent.setEventType("Leave_Group");
            notificationEvent.setUserId(user.getId());
            notificationEvent.setContent("You have leaved from " + group.getGroupName() + " group");
            notificationEvent.setEntityType("Group_Request");
            if(group.getPublic())
                notificationEvent.setEntityId(0);

            else
                notificationEvent.setEntityId(groupRequest.getId());


            notificationService.sendNotification(notificationEvent);


            return "Group leave request done successfully.";
        }
        catch (InternalServerException  e)
        {
            throw e;
        }

    }

    @Override
    public String respondGroupRequest(String token, GroupResponseToCreateDto groupDto)
    {
        try
        {

            User user = _AuthenticationService.authenticate(token , authenticationRepository);

            GroupRequest groupRequest = groupRequestRepository.getById(groupDto.getGroupRequestId());
            if(groupRequest == null)
                throw new NotFoundException("Group request not found");


            Group group = groupRequest.getGroup();
            User requestSender = groupRequest.getSender();



            GroupRequest oldGroupRequest = groupRequestRepository.findRequestByUserIdAndGroupId(requestSender.getId() , group.getId());
            if(oldGroupRequest.getId() != groupRequest.getId())
                throw new ClientException("This is an old version from group request.");

            if(groupRequest.getStatus() != RequestStatus.PENDING)
                throw new ClientException("The Request is already responded before.");

            if(!groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new UnAuthorizedException("You are not authorized to respond to this group.");

            if(groupDto.getResponseStatus().toLowerCase().equals("rejected"))
                groupRequest.setStatus(RequestStatus.REJECTED);

            else if (groupDto.getResponseStatus().toLowerCase().equals("approved"))
            {
                groupRequest.setStatus(RequestStatus.ACCEPTED);
                if(!groupRepository.isMemberInGroup(requestSender.getId(), group.getId()))
                    groupRepository.addMemberToGroup(requestSender.getId(), group.getId());

            }

            groupRequestRepository.update(groupRequest);

            return "Group request responded successfully.";
        }
        catch (InternalServerException e)
        {
            throw e;
        }


    }

    @Override
    public String createPostInGroup(String token, PostInGroupToCreateDto postInGroupDto)
    {

        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(postInGroupDto.getGroupId());

            if(group == null)
                throw new NotFoundException("Group not found");

            if(!groupRepository.isMemberInGroup(user.getId(), group.getId()) && !groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new UnAuthorizedException("You are not authorized to make posts in this group.");

            Post post = new Post();
            post.setAuthor(user);
            post.setGroup(group);
            post.setDescription(postInGroupDto.getDescription());

            if(postInGroupDto.getImageUrls() != null)
            {
                Set<Image> images = new HashSet<>();
                for (String url : postInGroupDto.getImageUrls()) {
                    Image image = new Image();
                    image.setUrl(url);
                    image.setPost(post); // if bidirectional mapping is used
                    images.add(image);
                }
                post.setImages(images);
            }

            if(postInGroupDto.getLinkPaths() != null)
            {
                Set<Link> links = new HashSet<>();
                for (String url : postInGroupDto.getLinkPaths()) {
                    Link link = new Link();
                    link.setPath(url);
                    link.setPost(post); // if bidirectional mapping is used
                    links.add(link);
                }
                post.setLinks(links);
            }

            postRepository.add(post);

            return "Post created successfully";
        }
        catch (InternalServerException e)
        {
            throw e;
        }


    }

    public String makeAdminInGroup(String token, int groupId, int memberId)
    {
        try {

            User admin = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(groupId);

            if(group == null){
                throw new NotFoundException("Group does not exist.");
            }

            if(!groupRepository.isAdminInGroup(admin.getId(), group.getId())){
                throw new UnAuthorizedException("You are not authorized to make this user the group admin.");
            }

            if(groupRepository.isAdminInGroup(memberId, group.getId())){
                throw new ClientException("User is already an admin of this group.");
            }

            if(!groupRepository.isMemberInGroup(memberId, group.getId())){
                throw new ClientException("The user you provided is not a member of this group.");
            }


            groupRepository.removeMemberFromGroup(memberId, groupId);

            groupRepository.addAdminToGroup(memberId, groupId);

            return "Successfully promoted user "+memberId+" as group admin.";

        }catch (InternalServerException e){
            throw e;
        }
    }

    @Override
    public String removeMemberFromGroup(String token , int groupId , int memberId)
    {
        try {

            User admin = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(groupId);

            if(group==null){
                throw new NotFoundException("Group does not exist.");
            }

            if(!groupRepository.isAdminInGroup(admin.getId(), group.getId())){
                throw new UnAuthorizedException("You are not authorized to remove this member from this group.");
            }

            if(groupRepository.isAdminInGroup(memberId, group.getId())){
                throw new UnAuthorizedException("You are not authorized to remove an admin from this group.");
            }

            if(!groupRepository.isMemberInGroup(memberId, group.getId())){
                throw new ClientException("The user you provided is not a member of this group.");
            }


            groupRepository.removeMemberFromGroup(memberId, groupId);

            return "Removed user "+memberId+" from group.";

        }catch (InternalServerException e){
            throw e;
        }
    }

    @Override
    public String deleteGroup(String token , int groupId)
    {
        try {

            User admin = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(groupId);

            if(group == null){
                throw new NotFoundException("Group does not exist.");
            }

            if (!groupRepository.isAdminInGroup(admin.getId(), group.getId())) {
                throw new UnAuthorizedException("You are not authorized to delete this group.");
            }

            group.getMembers().clear();
            group.getAdmins().clear();
            groupRepository.update(group);

            groupRepository.delete(groupId);


            return "Group " + groupId + " deleted successfully.";

        } catch (InternalServerException e) {
            throw e;
        }
    }

    @Override
    public String deletePostInGroup(String token, int groupId, int postId)
    {

        try
        {
            User user = _AuthenticationService.authenticate(token , authenticationRepository);

            Group group = groupRepository.getById(groupId);

            if(group == null)
                throw new NotFoundException("Group does not exist.");

            Post post = postRepository.getById(postId);

            if(post == null)
                throw new NotFoundException("Post does not exist.");

            if(post.getGroup() == null)
                throw new ClientException("This post is not in a group.");

            if(post.getGroup().getId() != group.getId())
                throw new ClientException("This post is not in this group.");

            if(!groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new UnAuthorizedException("You are not authorized to delete this post.");


            postRepository.delete(postId);

            return "Post " + postId + " deleted successfully.";
        }

        catch (InternalServerException e)
        {
            throw e;
        }

    }


}
