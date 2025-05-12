package Entities;


import jakarta.persistence.*;

@Entity
@Table(name = "Activity_Logs")
public class ActivityLog extends _BaseEntity
{
    private String activity;

    @Column(name = "entity_type")
    private String EntityType;

    @Column(name = "entity_id")
    private int EntityId;

    // Navigational Properties

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getEntityType() {
        return EntityType;
    }

    public void setEntityType(String entityType) {
        EntityType = entityType;
    }

    public int getEntityId() {
        return EntityId;
    }

    public void setEntityId(int entityId) {
        EntityId = entityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
