package DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

public class CommentToCreateDto implements Serializable
{
    @NotNull(message = "Post id is required.")
    private int postId;
    
    @NotNull(message = "Description cannot be null")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
    
    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL")String> imageUrls;
    
    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid link URL")String> linkPaths;
    
    // For replies - optional
    private Long parentCommentId;

    // Getters and Setters


    @NotNull(message = "Post id is required.")
    public int getPostId() {
        return postId;
    }

    public void setPostId(@NotNull(message = "Post id is required.") int postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
} 