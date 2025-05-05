package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import DTOs.FriendRequestToReceiveDto;
import DTOs.FriendToReturnDto;
import DTOs.FriendToSuggestDto;
import Entities.FriendRequest;
import Entities.User;
import Enums.RequestStatus;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.IConnectionRepository;
import RepositoriesContract.IUserRepository;
import ServicesContract.IConnectionService;
import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConnectionService implements IConnectionService
{
    @EJB
    private IConnectionRepository connectionRepository;

    @EJB
    private IUserRepository userRepository;

    @EJB
    private IAuthenticationRepository authenticationRepository;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String requestUser(String token, int receiverId)
    {

        try
        {
            User sender = _AuthenticationService.authenticate(token , authenticationRepository);

            User receiver = userRepository.getById(receiverId);

            if(receiver == null)
                throw new ClientException("There is no user with id "+receiverId);

            if(sender.getId() == receiver.getId())
                throw new ClientException("You can't request yourself.");

            FriendRequest friendRequest = connectionRepository.getFriendRequestBySenderIdAndReceiverId(sender.getId(), receiver.getId());
            if(friendRequest != null)
            {
                if(friendRequest.getRequest() == RequestStatus.PENDING)
                    throw new ClientException("The Request is already pending.");

                if (friendRequest.getRequest() == RequestStatus.ACCEPTED)
                    throw new ClientException("The Request is accepted before.");

            }

            FriendRequest newFriendRequest = new FriendRequest();
            newFriendRequest.setSender(sender);
            newFriendRequest.setReceiver(receiver);

            connectionRepository.add(newFriendRequest);

            return "The request sent successfully.";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Exception");
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<FriendRequestToReceiveDto> getFriendRequests(String token)
    {
        try
        {
            User receiver = _AuthenticationService.authenticate(token , authenticationRepository);

            List<FriendRequest> friendRequests = connectionRepository.getFriendRequestsReceivedByUserId(receiver.getId());
            List<FriendRequestToReceiveDto> friendRequestToReceiveDtos = new ArrayList<>();

            if(friendRequests == null)
                return friendRequestToReceiveDtos;

            for (FriendRequest friendRequest : friendRequests)
            {
                User sender = friendRequest.getSender();

                FriendRequestToReceiveDto friendRequestToReceiveDto = new FriendRequestToReceiveDto();
                friendRequestToReceiveDto.setFriendRequestId(friendRequest.getId());
                friendRequestToReceiveDto.setRequestStatus(friendRequest.getRequest().toString().toLowerCase());
                friendRequestToReceiveDto.setSenderId(sender.getId());
                friendRequestToReceiveDto.setSenderName(sender.getfName());
                friendRequestToReceiveDto.setSenderBio(sender.getBio());

                friendRequestToReceiveDtos.add(friendRequestToReceiveDto);
            }

            return friendRequestToReceiveDtos;

        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Exception");
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String acceptFriendRequest(String token, int friendRequestId)
    {
        try
        {
            User receiver = _AuthenticationService.authenticate(token , authenticationRepository);

            FriendRequest friendRequest = connectionRepository.getById(friendRequestId);
            if (friendRequest == null)
                throw new ClientException("There is no friend request with id "+friendRequestId);

            if(friendRequest.getReceiver().getId() != receiver.getId())
                throw new ClientException("You are not authorized to accept this friend request.");

            if(friendRequest.getRequest() != RequestStatus.PENDING)
                throw new ClientException("The Request has been already "+ friendRequest.getRequest().toString().toLowerCase() + " before.");

            friendRequest.setRequest(RequestStatus.ACCEPTED);
            connectionRepository.update(friendRequest);

            User sender = friendRequest.getSender();
            userRepository.addFriend(sender.getId(), receiver.getId());

            return "The request acceptance done successfully.";

        }

        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Exception");
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String rejectFriendRequest(String token, int friendRequestId)
    {
        try
        {
            User receiver = _AuthenticationService.authenticate(token , authenticationRepository);

            FriendRequest friendRequest = connectionRepository.getById(friendRequestId);
            if (friendRequest == null)
                throw new ClientException("There is no friend request with id "+friendRequestId);

            if(friendRequest.getReceiver().getId() != receiver.getId())
                throw new ClientException("You are not authorized to accept this friend request.");

            if(friendRequest.getRequest() != RequestStatus.PENDING)
                throw new ClientException("The Request has been already "+ friendRequest.getRequest().toString().toLowerCase() + " before.");

            friendRequest.setRequest(RequestStatus.REJECTED);
            connectionRepository.update(friendRequest);

            return "The request rejection done successfully.";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Exception");
        }

    }

    @Override
    public List<FriendToReturnDto> getFriends(String token)
    {

        try
        {
            User user = _AuthenticationService.authenticate(token , authenticationRepository);

            Set<User> friends = user.getFriends();

            List<FriendToReturnDto> friendToReturnDtos = new ArrayList<>();

            if(friends == null)
                return friendToReturnDtos;

            for (User friend : friends)
            {
                FriendToReturnDto friendToReturnDto = new FriendToReturnDto();
                friendToReturnDto.setFriendId(friend.getId());
                friendToReturnDto.setFriendName(friend.getfName());
                friendToReturnDto.setBio(friend.getBio());
                friendToReturnDto.setGender(friend.getGender().toString().toLowerCase());

                friendToReturnDtos.add(friendToReturnDto);
            }

            return friendToReturnDtos;

        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Exception");
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<FriendToSuggestDto> getFriendSuggestions(String token)
    {
        User user = _AuthenticationService.authenticate(token , authenticationRepository);

        Set<User> friends = user.getFriends();

        List<FriendToSuggestDto> friendToSuggestDtos = new ArrayList<>();

        if(friends == null)
            return friendToSuggestDtos;

       Set<Integer> excludedSuggestions = new HashSet<>();
       excludedSuggestions.add(user.getId());

       for (User friend : friends)
           excludedSuggestions.add(friend.getId());

        for (User friend : friends)
        {
            Set<User> friendOfFriends = friend.getFriends();
            for (User friendOfFriend : friendOfFriends)
            {
                if(!excludedSuggestions.contains(friendOfFriend.getId()))
                {
                    FriendToSuggestDto friendToSuggestDto = new FriendToSuggestDto();
                    friendToSuggestDto.setFriendSuggestId(friendOfFriend.getId());
                    friendToSuggestDto.setFriendSuggestName(friendOfFriend.getfName());
                    friendToSuggestDto.setFriendSuggestBio(friendOfFriend.getBio());
                    friendToSuggestDto.setFriendWith(friend.getfName() + " " + friend.getlName());

                    friendToSuggestDtos.add(friendToSuggestDto);
                    excludedSuggestions.add(friendOfFriend.getId());
                }

            }
        }

        return friendToSuggestDtos;
    }
}
