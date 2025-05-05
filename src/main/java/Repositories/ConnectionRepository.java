package Repositories;

import Entities.FriendRequest;
import Entities.User;
import Enums.RequestStatus;
import RepositoriesContract.IConnectionRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.PUT;

import java.util.List;

@Stateless
public class ConnectionRepository extends _GenericRepository<FriendRequest> implements IConnectionRepository
{
    public ConnectionRepository()
    {
        super(FriendRequest.class);
    }

    @Override
    public FriendRequest getFriendRequestBySenderIdAndReceiverId(int senderId, int receiverId)
    {
        TypedQuery<FriendRequest> friendRequestTypedQuery = entityManager.createQuery("SELECT f FROM FriendRequest f WHERE (f.sender.id = :senderId1 AND f.receiver.id = :receiverId1) OR (f.sender.id = :senderId2 AND f.receiver.id = :receiverId2)", FriendRequest.class);
        friendRequestTypedQuery.setParameter("senderId1", senderId);
        friendRequestTypedQuery.setParameter("receiverId1", receiverId);
        friendRequestTypedQuery.setParameter("senderId2", receiverId);
        friendRequestTypedQuery.setParameter("receiverId2", senderId);

        List<FriendRequest> friendRequests = friendRequestTypedQuery.getResultList();
        return friendRequests.isEmpty() ? null: friendRequests.getLast();
    }

    public List<FriendRequest> getFriendRequestsReceivedByUserId(int receiverId)
    {
        TypedQuery<FriendRequest> friendRequestTypedQuery = entityManager.createQuery("SELECT f FROM FriendRequest f WHERE f.receiver.id = :receiverId AND f.request =  :requestStatus", FriendRequest.class);
        friendRequestTypedQuery.setParameter("receiverId", receiverId);
        friendRequestTypedQuery.setParameter("requestStatus" , RequestStatus.PENDING);

        List<FriendRequest> friendRequests = friendRequestTypedQuery.getResultList();
        return friendRequests.isEmpty() ? null: friendRequests;

    }

}
