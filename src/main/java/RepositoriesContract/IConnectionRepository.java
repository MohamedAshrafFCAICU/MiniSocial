package RepositoriesContract;

import Entities.FriendRequest;

import java.util.List;

public interface IConnectionRepository extends _IGenericRepository<FriendRequest>
{
    public FriendRequest getFriendRequestBySenderIdAndReceiverId(int senderId, int receiverId);

    public List<FriendRequest> getFriendRequestsReceivedByUserId(int receiverId);
}