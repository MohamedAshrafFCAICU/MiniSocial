package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import DTOs.GroupResponseToCreateDto;
import DTOs.GroupToCreateDto;
import DTOs.PostInGroupToCreateDto;
import Entities.*;
import Enums.RequestStatus;
import Enums.Visability;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.IGroupRepository;
import RepositoriesContract.IGroupRequestRepository;
import RepositoriesContract.IPostRepository;
import ServicesContract.IGroupService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.HashSet;
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
            throw new InternalServerException("Internal server error");
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
                throw new ClientException("Group not found");

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
            {
                groupRepository.addMemberToGroup(user.getId(), group.getId());
                return "You are now member of this group";
            }


            User creator = group.getCreator();
            GroupRequest groupRequest = new GroupRequest();
            groupRequest.setGroup(group);
            groupRequest.setReceiver(creator);
            groupRequest.setSender(user);
            groupRequestRepository.add(groupRequest);

            return "Group join request done successfully.";
        }
        catch (InternalServerException e)
        {
            throw new InternalServerException("Internal server error");
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
                throw new ClientException("Group not found");

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


            return "Group leave request done successfully.";
        }
        catch (InternalServerException  e)
        {
            throw new InternalServerException("Internal server error");
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
                throw new ClientException("Group request not found");


            Group group = groupRequest.getGroup();
            User requestSender = groupRequest.getSender();



            GroupRequest oldGroupRequest = groupRequestRepository.findRequestByUserIdAndGroupId(requestSender.getId() , group.getId());
            if(oldGroupRequest.getId() != groupRequest.getId())
                throw new ClientException("This is an old version from group request.");

            if(groupRequest.getStatus() != RequestStatus.PENDING)
                throw new ClientException("The Request is already responded before.");

            if(!groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new ClientException("You are not authorized to respond to this group.");

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
            throw new InternalServerException("Internal server error");
        }


    }

    @Override
    public String createPostInGroup(String token, PostInGroupToCreateDto postInGroupDto)
    {

        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);

            Group group = groupRepository.getById(postInGroupDto.getGroupId());

            if(group == null)
                throw new ClientException("Group not found");

            if(!groupRepository.isMemberInGroup(user.getId(), group.getId()) && !groupRepository.isAdminInGroup(user.getId(), group.getId()))
                throw new ClientException("You are not authorized to make posts in this group.");

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
                postRepository.add(post);
            }

            return "Post created successfully";
        }
        catch (InternalServerException e)
        {
            throw new InternalServerException("Internal server error");
        }


    }
}
