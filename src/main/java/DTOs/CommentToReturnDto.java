package DTOs;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CommentToReturnDto implements Serializable {
    private Long commentId;
    private String description;
    private int authorId;
    private String authorName;
    private Date creationDate = new Date();
    private Set<String> imageUrls;
    private Set<String> linkPaths;
    private int parentCommentId;
    private Set<LikeToReturnDto> likesOnComment;



    public Set<LikeToReturnDto> getLikesOnComment() {
        return likesOnComment;
    }

    public void setLikesOnComment(Set<LikeToReturnDto> likesOnComment) {
        this.likesOnComment = likesOnComment;
    }

    public int getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(int parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    private Set<CommentToReturnDto> replies;

    // Getters and Setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {return authorName;}
    public void setAuthorName(String authorName) {this.authorName = authorName;}

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Set<String> getLinkPaths() {
        return linkPaths;
    }

    public void setLinkPaths(Set<String> linkPaths) {
        this.linkPaths = linkPaths;
    }

    public Set<CommentToReturnDto> getReplies() {
        return replies;
    }

    public void setReplies(Set<CommentToReturnDto> replies) {
        this.replies = replies;
    }
} 