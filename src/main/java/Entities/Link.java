package Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Links")
public class Link extends _BaseEntity
{

    @NotNull
    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY , optional = true)
    @JoinColumn(name = "post_id" ,nullable = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "comment_id" , nullable = false)
    private Comment comment;


    public  String getPath() {
        return path;
    }

    public void setPath( String path) {
        this.path = path;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


}
