package DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CommentToUpdateDto {

    private int commentId;

    @NotNull(message = "Description cannot be null")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL")String> imageUrls;

    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid link URL")String> linkPaths;


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public @NotNull(message = "Description cannot be null") @Size(max = 500, message = "Description must be at most 500 characters") String getDescription() {
        return description;
    }

    public void setDescription(@NotNull(message = "Description cannot be null") @Size(max = 500, message = "Description must be at most 500 characters") String description) {
        this.description = description;
    }

    public Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL") String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL") String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid link URL") String> getLinkPaths() {
        return linkPaths;
    }

    public void setLinkPaths(Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid link URL") String> linkPaths) {
        this.linkPaths = linkPaths;
    }

}
