package RepositoriesContract;

import Entities.Comment;


public interface ICommentRepository extends _IGenericRepository<Comment>
{
    public Comment getCommentByUserAndPostId(int userId, int postId);
} 