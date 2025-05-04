package Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Images")
public class Image extends _BaseEntity
{
    @NotNull
    @Column(nullable = false)
    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid URL format")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "comment_id" , nullable = false)
    private Comment comment;

    public String getUrl() {
        return url;
    }

    public void setUrl( String url) {
        this.url = url;
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
