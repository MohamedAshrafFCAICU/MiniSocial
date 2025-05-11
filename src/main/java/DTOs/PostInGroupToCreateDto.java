package DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class PostInGroupToCreateDto
{
    @NotNull(message = "Group id is required.")
    private int groupId;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL")String> imageUrls;

    private Set<@Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid link URL") String> linkPaths;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public @Size(max = 1000, message = "Description must be at most 1000 characters") String getDescription() {
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

}
