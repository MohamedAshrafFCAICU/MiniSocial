package Repositories;

import Entities.Comment;
import RepositoriesContract.ICommentRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CommentRepository extends _GenericRepository<Comment> implements ICommentRepository
{
    public CommentRepository()
    {
        super(Comment.class);
    }

} 