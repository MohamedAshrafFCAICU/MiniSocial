package RepositoriesContract;

import Entities.GroupRequest;

public interface IGroupRequestRepository extends _IGenericRepository<GroupRequest>  {
    public GroupRequest findRequestByUserIdAndGroupId(int userId , int groupId);
}
