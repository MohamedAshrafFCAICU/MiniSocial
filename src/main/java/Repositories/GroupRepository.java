package Repositories;

import Entities.Group;
import RepositoriesContract.IGroupRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class GroupRepository extends _GenericRepository<Group> implements IGroupRepository
{
    public GroupRepository()
    {
        super(Group.class);
    }

    @Override
    public Group getGroupByName(String groupName)
    {
        TypedQuery<Group> query = entityManager.createQuery("SELECT g FROM Group g WHERE g.groupName = :name", Group.class);
        query.setParameter("name", groupName);

        List<Group> groups = query.getResultList();

        return groups.isEmpty() ? null : groups.get(0);
    }

    @Override
    public void addAdminToGroup(int adminId, int groupId) {
        Query query1 = entityManager.createNativeQuery("INSERT INTO Admin_Group (user_id, group_id) VALUES (?, ?)", Group.class);
        query1.setParameter(1, adminId);
        query1.setParameter(2, groupId);
        query1.executeUpdate();

    }

    @Override
    public void addMemberToGroup(int memberId, int groupId)
    {
        Query query1 = entityManager.createNativeQuery("INSERT INTO Member_Group (user_id, group_id) VALUES (?, ?)", Group.class);
        query1.setParameter(1, memberId);
        query1.setParameter(2, groupId);
        query1.executeUpdate();
    }

    @Override
    public Boolean isAdminInGroup(int adminId, int groupId) {
        Query query = entityManager.createNativeQuery("SELECT 1 FROM Admin_Group WHERE group_id = ? AND admin_id = ?");
        query.setParameter(1, groupId);
        query.setParameter(2, adminId);
        List<?> result = query.getResultList();
        return !result.isEmpty();


    }

    @Override
    public Boolean isMemberInGroup(int memberId, int groupId) {
        Query query = entityManager.createNativeQuery("SELECT 1 FROM Member_Group WHERE group_id = ? AND user_id = ?");
        query.setParameter(1, groupId);
        query.setParameter(2, memberId);
        List<?> result = query.getResultList();
        return !result.isEmpty();
    }


}

