package Repositories;

import Entities.Like;
import Enums.LikeType;
import RepositoriesContract.ILikeRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class LikeRepository extends _GenericRepository<Like> implements ILikeRepository
{
    public LikeRepository()
    {
        super(Like.class);
    }

    @Override
    public boolean existsByUserAndPost(int userId, int postId) {
        String query = "SELECT COUNT(l) FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByUserAndComment(int userId, int commentId) {
        String query = "SELECT COUNT(l) FROM Like l WHERE l.user.id = :userId AND l.comment.id = :commentId";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userId", userId)
                .setParameter("commentId", commentId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Like getLikeByUserAndPost(int userId, int postId)
    {
        TypedQuery<Like> query = entityManager.createQuery("SELECT l FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId", Like.class);
        query.setParameter("userId", userId);
        query.setParameter("postId", postId);

        List<Like> likes = query.getResultList();

        return likes.isEmpty() ? null : likes.getLast();

    }

} 