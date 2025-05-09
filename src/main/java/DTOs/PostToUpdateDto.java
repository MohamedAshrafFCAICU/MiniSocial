package DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

public class PostToUpdateDto implements Serializable {
    private int postId;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;


    @Pattern(
            regexp = "\\s*(GLOBAL|FRIENDS|ONLYME)\\s*",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Visability must be one of: GLOBAL, FRIENDS, ONLYME (case-insensitive, trim allowed)"
    )
    private String visability;

    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL")String> imageUrls;

    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid link URL") String> linkPaths;

    // Getters and Setters
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
