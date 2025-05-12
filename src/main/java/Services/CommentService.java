package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import CustomizedExceptions.UnAuthorizedException;
import DTOs.CommentToCreateDto;
import DTOs.CommentToUpdateDto;
import DTOs.NotificationEventToPassDto;
import Entities.*;
import Enums.Visability;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.ICommentRepository;
import RepositoriesContract.IPostRepository;
import ServicesContract.ICommentService;
import ServicesContract.INotificationService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.HashSet;
import java.util.Set;

@Stateless
public class CommentService implements ICommentService {

    @EJB
    private ICommentRepository commentRepository;

    @EJB
    private IPostRepository postRepository;

    @EJB
    private INotificationService notificationService;

    @EJB
    private IAuthenticationRepository authenticationRepository;

    @Override
    public String addCommentToPost(String token, CommentToCreateDto commentToCreateDto)
    {
        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Post post = postRepository.getById(commentToCreateDto.getPostId());

            if (post == null) throw new ClientException("Post not found");

            if(post.getVisability() == Visability.ONLYME && post.getAuthor().getId() != user.getId())
                throw new UnAuthorizedException("You are not authorized to comment on this post");
            if(post.getVisability() == Visability.FRIENDS && post.getAuthor().getId() != user.getId())
            {
                Set<User> friends = post.getAuthor().getFriends();
                boolean isFriend = false;
                for (User u : friends) {
                    if(u.getId() == user.getId())
                        isFriend = true;
                }
                if (!isFriend) {
                    throw new ClientException("You are not authorized to comment on this post");
                }
            }

            Comment comment = new Comment();
            comment.setUser(user);
            comment.setPost(post);
            comment.setDescription(commentToCreateDto.getDescription());
            Set<Image> images = new HashSet<>();
            if (commentToCreateDto.getImageUrls() != null) {
                for (String url : commentToCreateDto.getImageUrls()) {
                    Image image = new Image();
                    image.setUrl(url);
                    image.setComment(comment); // if bidirectional mapping
                    images.add(image);
                }
            }
            comment.setImages(images);
            Set<Link> links = new HashSet<>();
            if (commentToCreateDto.getLinkPaths() != null) {
                for (String path : commentToCreateDto.getLinkPaths()) {
                    Link link = new Link();
                    link.setPath(path);
                    link.setComment(comment); // if bidirectional mapping
                    links.add(link);
                }
            }
            comment.setLinks(links);

            if (commentToCreateDto.getParentCommentId() != null) {
                Comment parent = commentRepository.getById(commentToCreateDto.getParentCommentId().intValue());
                if (parent == null)
                    throw new ClientException("Parent comment not found");
                if(parent.getPost().getId() != commentToCreateDto.getParentCommentId().intValue())
                    throw new UnAuthorizedException("The parent comment is not in this post.");
                comment.setParentComment(parent);
            }
            commentRepository.add(comment);

            NotificationEventToPassDto notificationEvent = new NotificationEventToPassDto();
            notificationEvent.setEventType("Comment_Post");
            notificationEvent.setUserId(post.getAuthor().getId());
            notificationEvent.setContent(user.getfName() + " " + user.getlName() + " has commented on your post.");
            notificationEvent.setEntityType("Comments");
            notificationEvent.setEntityId(commentRepository.getCommentByUserAndPostId(user.getId() , post.getId()).getId());

            notificationService.sendNotification(notificationEvent);

            return "Comment added successfully";
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }
    }

    @Override
    public String updateCommentToPost(String token, CommentToUpdateDto commentToUpdateDto)
    {
        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Comment existing = commentRepository.getById(commentToUpdateDto.getCommentId());
            if (existing == null)
                throw new ClientException("Comment not found");

            if (existing.getUser().getId() != user.getId()) throw new ClientException("You are not authorized to update this comment");

            existing.setDescription(commentToUpdateDto.getDescription());
            Set<Image> updatedImages = new HashSet<>();
            if (commentToUpdateDto.getImageUrls() != null) {
                for (String url : commentToUpdateDto.getImageUrls()) {
                    Image image = new Image();
                    image.setUrl(url);
                    image.setComment(existing); // bidirectional
                    updatedImages.add(image);
                }
            }
            existing.setImages(updatedImages);
            Set<Link> updatedLinks = new HashSet<>();
            if (commentToUpdateDto.getLinkPaths() != null) {
                for (String path : commentToUpdateDto.getLinkPaths()) {
                    Link link = new Link();
                    link.setPath(path);
                    link.setComment(existing); // bidirectional
                    updatedLinks.add(link);
                }
            }
            existing.setLinks(updatedLinks);

            commentRepository.update(existing);
            return "Comment updated successfully";
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }
    }

    @Override
    public String deleteCommentToPost(String token, int commentId)
    {
        try {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Comment comment = commentRepository.getById(commentId);
            if (comment == null) throw new ClientException("Comment not found");
            if (comment.getUser().getId() != user.getId()) throw new ClientException("You are not authorized to delete this comment");
            commentRepository.delete(commentId);
            return "Comment deleted successfully";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal server error");
        }
    }
}
