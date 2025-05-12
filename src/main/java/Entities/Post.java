package Entities;


import Enums.PostStatus;
import Enums.Visability;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Posts")
public class Post extends _BaseEntity
{

    @Column(nullable = true)
    private String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visability visability = Visability.GLOBAL;

    @NotNull
    @Column(nullable = false , name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.POSTED;


    // Navigational Properties:
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User author;

    // *** Post has Image URL *** //
    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Image> images = new HashSet<>();

    // *** Post has Link URL *** //
    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Link> links = new HashSet<>();

    // *** Post has Likes *** //
    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private Set<Like> likes = new HashSet<>();

    // *** Post has Comments *** //
    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE ,  fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(optional = true, fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id" , nullable = true)
    private Group group;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public  Visability getVisability() {
        return visability;
    }

    public void setVisability(Visability visability) {
        this.visability = visability;
    }

    public  PostStatus getStatus() {
        return status;
    }

    public void setStatus( PostStatus status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
