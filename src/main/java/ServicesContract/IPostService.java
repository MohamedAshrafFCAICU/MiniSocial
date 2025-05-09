package ServicesContract;

import DTOs.PostToCreateDto;
import DTOs.PostToReturnDto;
import DTOs.PostToUpdateDto;

import java.util.List;

public interface IPostService {
    public String createPost(String token, PostToCreateDto postToCreateDto);
    public String updatePost(String token, PostToUpdateDto postToUpdateDto);
    public String deletePost(String token, int postId);
    public List<PostToReturnDto> getUserFeed(String token);

}
