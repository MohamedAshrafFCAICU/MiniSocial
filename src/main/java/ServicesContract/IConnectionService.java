package ServicesContract;

import DTOs.FriendRequestToReceiveDto;
import DTOs.FriendToReturnDto;
import DTOs.FriendToSuggestDto;
import Entities.FriendRequest;

import java.util.List;
import java.util.Set;

public interface IConnectionService
{
    public String requestUser(String token , int receiverId);

    public List<FriendRequestToReceiveDto> getFriendRequests(String token);

    public String acceptFriendRequest(String token , int friendRequestId);

    public String rejectFriendRequest(String token , int friendRequestId);

    public List<FriendToReturnDto> getFriends(String token);

    public List<FriendToSuggestDto> getFriendSuggestions(String token);
}
