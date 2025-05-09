package RepositoriesContract;

import Entities.Like;
import Enums.LikeType;

import java.util.Map;

public interface ILikeRepository extends _IGenericRepository<Like>
{
   public boolean existsByUserAndPost(int userId, int postId);

   public boolean existsByUserAndComment(int userId, int commentId);
} 