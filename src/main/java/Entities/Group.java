package Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Groups")
public class Group extends _BaseEntity
{
    @NotNull
    @Column(nullable = false)
    @Size(min = 3 , max = 500)
    private String description;

    @NotNull
    @Column(nullable = false , unique = true)
    private String groupName;

    @Column(nullable = false , name = "is_public")
    private Boolean isPublic = true;

    // Navigational Properties:
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "creator_id" , nullable = false)
    private User creator = new User();

    @ManyToMany(mappedBy = "memberInGroups" , fetch = FetchType.LAZY)
    private Set<User> members = new HashSet<>();

    @ManyToMany(mappedBy = "adminInGroups" ,fetch = FetchType.LAZY)
    private Set<User> admins = new HashSet<>();

    // *** Group has Group Requests *** //
    @OneToMany(mappedBy = "group" , fetch = FetchType.LAZY)
    private Set<GroupRequest> groupRequests= new HashSet<>();



    public  String getDescription() {
        return description;
    }

    public void setDescription( String description) {
        this.description = description;
    }

    public  String getGroupName() {
        return groupName;
    }

    public void setGroupName( String groupName) {
        this.groupName = groupName;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<User> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<User> admins) {
        this.admins = admins;
    }

    public Set<GroupRequest> getGroupRequests() {
        return groupRequests;
    }

    public void setGroupRequests(Set<GroupRequest> groupRequests) {
        this.groupRequests = groupRequests;
    }



}
