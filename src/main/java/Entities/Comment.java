package Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Comments")
public class Comment extends _BaseEntity
{

    @Column(nullable = true)
    private String description;

    // Navigational Properties:
    // *** Comment has Image URL *** //
    @OneToMany(mappedBy = "comment" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private Set<Image> images = new HashSet<>();

    // *** Comment has Image URL *** //
    @OneToMany(mappedBy = "comment" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private Set<Link> links = new HashSet<>();

    // *** Comment has Likes *** //
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Like> likes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "post_id" , nullable = false)
    private Post post;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // *** Comment has Nested Comment (Reply) *** //
    @ManyToOne(fetch = FetchType.LAZY , optional = true)
    @JoinColumn(name = "parent_comment_id" , nullable = true)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private Set<Comment> replies = new HashSet<>();

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

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getReplies() {
        return replies;
    }

    public void setReplies(Set<Comment> replies) {
        this.replies = replies;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }


}
