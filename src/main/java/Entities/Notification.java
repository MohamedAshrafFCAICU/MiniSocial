package Entities;


import jakarta.persistence.*;

@Entity
@Table(name = "Notifications")
public class Notification extends _BaseEntity
{
    @Column(name = "event_type")
    private String eventType;

    private String content;

    @Column(name = "is_read")
    private  Boolean isRead = false;

    @Column(name = "entity_type")
    private String entityType;

    private int entityId;

    // Navigational Properties:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
