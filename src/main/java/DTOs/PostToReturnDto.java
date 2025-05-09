package DTOs;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class PostToReturnDto implements Serializable {
    private Long postId;
    private String description;
    private String visability;
    private String authorUsername;
    private Date creationDate = new Date();
    private Set<String> imageUrls;
    private Set<String> linkPaths;
    private Set<LikeToReturnDto> likesOnPost;
    private long likesCount;
    private Set<CommentToReturnDto> comments;


    // Getters and Setters

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisability() {
        return visability;
    }

    public void setVisability(String visability) {
        this.visability = visability;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

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

    public Set<LikeToReturnDto> getLikesOnPost() {
        return likesOnPost;
    }

    public void setLikesOnPost(Set<LikeToReturnDto> likesOnPost) {
        this.likesOnPost = likesOnPost;
    }

    public Set<CommentToReturnDto> getComments() {
        return comments;
    }

    public void setComments(Set<CommentToReturnDto> comments) {
        this.comments = comments;
    }
}
