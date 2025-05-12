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

    @Override
    public Comment getCommentByUserAndPostId(int userId, int postId)
    {
        TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.post.id = :postId", Comment.class);
        query.setParameter("userId", userId);
        query.setParameter("postId", postId);
        List<Comment> comments = query.getResultList();

        return comments.isEmpty() ? null : comments.getLast();
    }
}