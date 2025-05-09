package DTOs;

import java.io.Serializable;

public class LikeForCommentToCreateDto implements Serializable {

    private Long commentId;

    private String likeType = "LIKE";

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }
}
