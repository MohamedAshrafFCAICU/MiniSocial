package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import CustomizedExceptions.UnAuthorizedException;
import DTOs.LikeForCommentToCreateDto;
import DTOs.LikeForPostToCreateDto;
import Entities.Comment;
import Entities.Like;
import Entities.Post;
import Entities.User;
import Enums.LikeType;
import Enums.Visability;
import Repositories.CommentRepository;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.ICommentRepository;
import RepositoriesContract.ILikeRepository;
import RepositoriesContract.IPostRepository;
import ServicesContract.ILikeService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;
import java.util.Set;

@Stateless
public class LikeService implements ILikeService {

    @EJB
    private ILikeRepository likeRepository;
    @EJB
    private ICommentRepository commentRepository;
    @EJB
    private IPostRepository postRepository;
    @EJB
    private IAuthenticationRepository authenticationRepository;

    @Override
    public String addLikeForPost(String token, LikeForPostToCreateDto likeForPostToCreateDto) {

        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Post post = postRepository.getById(likeForPostToCreateDto.getPostId().intValue());
            if (post == null) {
                throw new ClientException("Post not found");
            }
            if(post.getVisability() == Visability.ONLYME && post.getAuthor().getId() != user.getId())
                throw new UnAuthorizedException("You are not authorized to like this post");
            if(post.getVisability() == Visability.FRIENDS && post.getAuthor().getId() != user.getId())
            {
                Set<User> friends = post.getAuthor().getFriends();
                boolean isFriend = false;
                for (User u : friends) {
                    if(u.getId() == user.getId())
                        isFriend = true;
                }
                if (!isFriend) {
                    throw new ClientException("You are not authorized to like this post");
                }
            }

            // Prevent duplicate likes
            if ( likeRepository.existsByUserAndPost(user.getId(), post.getId()) ) {
                throw new ClientException("You already liked this post");
            }
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            try {
                like.setLikeType(LikeType.valueOf(likeForPostToCreateDto.getLikeType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ClientException("Invalid like type");
            }
            likeRepository.add(like);
            return "Like added to post successfully";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }


    }

    @Override
    public String removeLikeForPost(String token, int LikeId) {

        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Like like = likeRepository.getById(LikeId);
            if (like == null) {
                throw new ClientException("Like not found");
            }
            // Check that the like belongs to the authenticated user
            if (like.getUser().getId() != user.getId()) {
                throw new ClientException("You are not authorized to remove this like");
            }
            likeRepository.delete(like.getId());
            return "Like removed successfully";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }
    }

    @Override
    public String addLikeForComment(String token, LikeForCommentToCreateDto likeForCommentToCreateDto) {

        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Comment comment = commentRepository.getById(likeForCommentToCreateDto.getCommentId().intValue());

            if (comment == null) {
                throw new ClientException("Comment not found");
            }

            Post post = comment.getPost();

            if(post.getVisability() == Visability.ONLYME && post.getAuthor().getId() != user.getId())
                throw new UnAuthorizedException("You are not authorized to like this comment.");
            if(post.getVisability() == Visability.FRIENDS && post.getAuthor().getId() != user.getId())
            {
                Set<User> friends = post.getAuthor().getFriends();
                boolean isFriend = false;
                for (User u : friends) {
                    if(u.getId() == user.getId())
                        isFriend = true;
                }
                if (!isFriend) {
                    throw new ClientException("You are not authorized to like this comment.");
                }
            }

            if (likeRepository.existsByUserAndComment(user.getId(), comment.getId())) {
                throw new ClientException("You already liked this comment");
            }
            Like like = new Like();
            like.setComment(comment);
            like.setUser(user);
            try {
                like.setLikeType(LikeType.valueOf(likeForCommentToCreateDto.getLikeType().toUpperCase()));
            }
            catch (IllegalArgumentException e) {
                throw new ClientException("Invalid like type");
            }
            likeRepository.add(like);
            return "Like added to comment successfully";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }
    }

    @Override
    public String removeLikeForComment(String token, int likeId) {
        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Like like = likeRepository.getById(likeId);
            if (like == null || like.getComment() == null) {
                throw new ClientException("Like on comment not found");
            }
            if (like.getUser().getId() != user.getId()) {
                throw new ClientException("You are not authorized to remove this like");
            }
            likeRepository.delete(like.getId());
            return "Like removed from comment successfully";
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }
    }
}
