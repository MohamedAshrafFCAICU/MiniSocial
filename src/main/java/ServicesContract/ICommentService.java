package ServicesContract;

import DTOs.CommentToCreateDto;
import DTOs.CommentToUpdateDto;

public interface ICommentService
{
    public String addCommentToPost(String token, CommentToCreateDto commentToCreateDto);
    public String updateCommentToPost(String token, CommentToUpdateDto commentToUpdateDto);
    public String deleteCommentToPost(String token, int commentId);

}
