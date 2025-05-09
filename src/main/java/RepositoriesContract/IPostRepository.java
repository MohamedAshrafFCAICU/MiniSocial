package RepositoriesContract;

import Entities.Post;

import java.util.List;
import java.util.Set;


public interface IPostRepository extends _IGenericRepository<Post>
{
   public List<Post> getFeedPosts(Integer userId, Set<Integer> friendIds);
}