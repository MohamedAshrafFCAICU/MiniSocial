package RepositoriesContract;

import Entities.ActivityLog;


import java.util.List;

    public interface IActivityLogRepository extends _IGenericRepository<ActivityLog>
{
    public List<ActivityLog> getRecentActivityLogs(int recentNumber);

    public List<ActivityLog> getActivityLogsByUserId(int userId , int recentNumber);
}
