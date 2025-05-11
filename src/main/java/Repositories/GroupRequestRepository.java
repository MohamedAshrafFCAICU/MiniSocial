package Repositories;

import Entities.GroupRequest;
import RepositoriesContract.IGroupRequestRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class GroupRequestRepository extends _GenericRepository<GroupRequest> implements IGroupRequestRepository {
    public GroupRequestRepository()
    {
        super(GroupRequest.class);
    }

    @Override
    public GroupRequest findRequestByUserIdAndGroupId(int userId , int groupId) {
        TypedQuery<GroupRequest> query = entityManager.createQuery("SELECT r FROM GroupRequest r WHERE r.sender.id = :userId AND r.group.id = :groupId", GroupRequest.class);
        query.setParameter("userId", userId);
        query.setParameter("groupId", groupId);
        List<GroupRequest> result = query.getResultList();
        return result.isEmpty() ? null : result.getLast();
    }
}
