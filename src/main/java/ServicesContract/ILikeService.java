package ServicesContract;


import DTOs.LikeForCommentToCreateDto;
import DTOs.LikeForPostToCreateDto;

public interface ILikeService {
    public String addLikeForPost(String token, LikeForPostToCreateDto likeForPostToCreateDto);
    public String removeLikeForPost(String token, int LikeId);
    public String addLikeForComment(String token, LikeForCommentToCreateDto likeForCommentToCreateDto);
    public String removeLikeForComment(String token, int LikeId);

}
