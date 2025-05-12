package RepositoriesContract;

import Entities.Group;
import Entities.Post;

public interface IGroupRepository extends _IGenericRepository<Group>
{
    public Group getGroupByName(String groupName);

    public void addAdminToGroup(int adminId, int groupId);

    public void addMemberToGroup(int memberId, int groupId);

    public Boolean isAdminInGroup(int adminId, int groupId);

    public Boolean isMemberInGroup(int memberId, int groupId);

    public void removeMemberFromGroup(int memberId, int groupId);


}
