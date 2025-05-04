package Entities;

import Enums.LikeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Likes")
public class Like extends _BaseEntity
{
    @NotNull
    @Column(nullable = false , name = "like_type")
    @Enumerated(EnumType.STRING)
    private LikeType likeType;


    // Navigational Properties:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;


    public  LikeType getLikeType() {
        return likeType;
    }

    public void setLikeType( LikeType likeType) {
        this.likeType = likeType;
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


}
