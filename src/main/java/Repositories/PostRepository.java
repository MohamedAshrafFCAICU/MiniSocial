package Repositories;

import Entities.Post;
import RepositoriesContract.IPostRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;


import java.util.List;
import java.util.Set;

@Stateless
public class PostRepository extends _GenericRepository<Post> implements IPostRepository
{
    public PostRepository()
    {
        super(Post.class);
    }

    @Override
public List<Post> getFeedPosts(Integer userId, Set<Integer> friendIds) {

    TypedQuery<Post> query = entityManager.createQuery(
                    "SELECT p FROM Post p WHERE " +
                            "p.visability = 'GLOBAL' AND p.group IS null " +
                            "OR (p.visability = 'FRIENDS' AND p.author.id IN :friendIds ) " +
                            "OR (p.visability != 'GLOBAL' AND p.author.id = :userId) ", Post.class)
            .setParameter("friendIds", friendIds)
            .setParameter("userId", userId);
    List<Post> posts = query.getResultList();

    return posts == null ? null : posts;
}
}