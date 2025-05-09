package DTOs;

import java.io.Serializable;

public class LikeForPostToCreateDto implements Serializable
{

    private Long postId;

    private String likeType = "LIKE";

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }
}
