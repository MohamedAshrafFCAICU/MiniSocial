package Entities;


import Enums.Gender;
import Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User extends _BaseEntity
{
    @NotNull
    @Column(nullable = false , name = "first_name")
    private String fName;

    @Column(nullable = true , name = "last_name")
    private String lName;

    @Email
    @NotNull
    @Column(nullable = false , unique = true)
    private String email;

    @NotNull
    @Column(nullable = false , name = "hashed_password")
    private String hashedPassword;

    @Size(min = 3, max = 100)
    @Column(nullable = true)
    private String bio;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Min(12)
    @NotNull
    @Column(nullable = false)
    private int age;

    @Column(nullable = true , name = "phone_number")
    @Pattern(regexp = "01[0125][0-9]{8}", message = "Invalid Egyptian phone number")
    private String phoneNumber;

    @NotNull
    @Column(nullable = false , name = "is_active")
    private Boolean isActive = true;


    // Navigational Properties:
    @OneToOne(optional = true , cascade = CascadeType.ALL , fetch = FetchType.LAZY )
    @JoinColumn( name = "authentication_id")
    private Authentication authentication = new Authentication();

    // *** User Can Make a Friend Request *** //
    @OneToMany(mappedBy = "sender" , fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private Set<FriendRequest> friendRequestSent = new HashSet<>();

    // *** User Receives a Friend Request *** //
    @OneToMany(mappedBy = "receiver" , fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private Set<FriendRequest> friendRequestReceived = new HashSet<>();

    // *** User become a Friend with another User *** //
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="Friends",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="friend_id"))
    private Set<User> friends = new HashSet<>();

    // *** User Can Create a Post *** //
    @OneToMany(mappedBy = "author" , cascade = CascadeType.REMOVE , orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    // *** User Can Make a Like *** //
    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private Set<Like> likes = new HashSet<>();

    // *** User Can Make a Comment *** //
    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    // *** User(Admin) Can create a group *** //
    @OneToMany(mappedBy = "creator" , fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    // *** User Can Join a Group *** //
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name="Member_Group",
            joinColumns=@JoinColumn(name="user_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="group_id", nullable = false))
    private Set<Group> memberInGroups = new HashSet<>();

    // *** User Can Admin For Group *** //
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name="Admin_Group",
            joinColumns=@JoinColumn(name="admin_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="group_id", nullable = false))
    private Set<Group> adminInGroups = new HashSet<>();

    // *** User Can request to join Group *** //
    @OneToMany(mappedBy = "sender" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Set<GroupRequest> groupRequestSent = new HashSet<>();

    // *** Admin Receive Group Request *** //
    @OneToMany(mappedBy = "receiver" , fetch = FetchType.LAZY )
    private Set<GroupRequest> groupRequestReceived = new HashSet<>();


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName( String lName) {
        this.lName = lName;
    }

    public  String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public  String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword( String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getBio() {
        return bio;
    }

    public void setBio( String bio) {
        this.bio = bio;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public  Role getRole() {
        return role;
    }

    public void setRole( Role role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge( int age) {
        this.age = age;
    }

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public  Boolean getActive() {
        return isActive;
    }

    public void setActive( Boolean active) {
        isActive = active;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Set<FriendRequest> getFriendRequestSent() {
        return friendRequestSent;
    }

    public void setFriendRequestSent(Set<FriendRequest> friendRequestSent) {
        this.friendRequestSent = friendRequestSent;
    }

    public Set<FriendRequest> getFriendRequestReceived() {
        return friendRequestReceived;
    }

    public void setFriendRequestReceived(Set<FriendRequest> friendRequestReceived) {
        this.friendRequestReceived = friendRequestReceived;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Group> getMemberInGroups() {
        return memberInGroups;
    }

    public void setMemberInGroups(Set<Group> memberInGroups) {
        this.memberInGroups = memberInGroups;
    }

    public Set<Group> getAdminInGroups() {
        return adminInGroups;
    }

    public void setAdminInGroups(Set<Group> adminInGroups) {
        this.adminInGroups = adminInGroups;
    }

    public Set<GroupRequest> getGroupRequestSent() {
        return groupRequestSent;
    }

    public void setGroupRequestSent(Set<GroupRequest> groupRequestSent) {
        this.groupRequestSent = groupRequestSent;
    }

    public Set<GroupRequest> getGroupRequestReceived() {
        return groupRequestReceived;
    }

    public void setGroupRequestReceived(Set<GroupRequest> groupRequestReceived) {
        this.groupRequestReceived = groupRequestReceived;
    }


}
