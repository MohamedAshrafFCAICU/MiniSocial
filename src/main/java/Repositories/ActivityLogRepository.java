package Repositories;

import Entities.ActivityLog;
import RepositoriesContract.IActivityLogRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;


@Stateless
public class ActivityLogRepository extends _GenericRepository<ActivityLog> implements IActivityLogRepository
{
    public ActivityLogRepository()
    {
        super(ActivityLog.class);
    }


    public List<ActivityLog> getRecentActivityLogs(int recentNumber)
    {
        TypedQuery<ActivityLog> query = entityManager.createQuery("SELECT a FROM ActivityLog a ORDER BY a.creationDate DESC ", ActivityLog.class);
        query.setMaxResults(recentNumber);

        List<ActivityLog> activityLogs = query.getResultList();

        return activityLogs.isEmpty()? null: activityLogs;
    }

    public List<ActivityLog> getActivityLogsByUserId(int userId , int recentNumber)
    {
        TypedQuery<ActivityLog> query = entityManager.createQuery("SELECT a FROM ActivityLog a WHERE a.user.id = :userId  ORDER BY a.creationDate DESC ", ActivityLog.class);
        query.setParameter("userId", userId);
        query.setMaxResults(recentNumber);

        List<ActivityLog> activityLogs = query.getResultList();

        return activityLogs.isEmpty()? null: activityLogs;
    }


}
