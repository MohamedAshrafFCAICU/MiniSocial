package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import DTOs.*;
import Entities.Image;
import Entities.Link;
import Entities.Post;
import Entities.User;
import Enums.PostStatus;
import Enums.Visability;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.IPostRepository;
import ServicesContract.IPostService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class PostService implements IPostService
{

    @EJB
    private IAuthenticationRepository authenticationRepository;
    @EJB
    private IPostRepository postRepository;

    @Override
    public String createPost(String token, PostToCreateDto postDto)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Post post = new Post();
            post.setAuthor(user);
            post.setDescription(postDto.getDescription());
            post.setVisability(postDto.getVisability().trim().toUpperCase().equals("GLOBAL") ? Visability.GLOBAL :
                    postDto.getVisability().trim().toUpperCase().equals("FRIENDS")  ? Visability.FRIENDS : Visability.ONLYME);

            Set<Image> images = new HashSet<>();
            for (String url : postDto.getImageUrls()) {
                Image image = new Image();
                image.setUrl(url);
                image.setPost(post); // if bidirectional mapping is used
                images.add(image);
            }
            post.setImages(images);

            Set<Link> links = new HashSet<>();
            for (String url : postDto.getLinkPaths()) {
                Link link = new Link();
                link.setPath(url);
                link.setPost(post); // if bidirectional mapping is used
                links.add(link);
            }
            post.setLinks(links);
            postRepository.add(post);


            return "Post Created Successfully";

        } catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Error");
        }
    }

    @Override
    public String updatePost(String token, PostToUpdateDto postDto)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Post post = postRepository.getById(postDto.getPostId());
            if (post == null) throw new ClientException("Post Not Found");
            User author = post.getAuthor();
            if (!author.equals(user)) throw new ClientException("You do not have permission to update this post");
            if (postDto.getDescription() != null) {
                post.setDescription(postDto.getDescription());
            }
            if (postDto.getVisability() != null) {
                String vis = postDto.getVisability().trim().toUpperCase();
                switch (vis) {
                    case "GLOBAL":
                        post.setVisability(Visability.GLOBAL);
                        break;
                    case "FRIENDS":
                        post.setVisability(Visability.FRIENDS);
                        break;
                    case "ONLYME":
                        post.setVisability(Visability.ONLYME);
                        break;
                    default:
                        throw new ClientException("Invalid visibility value");
                }
            }
            if (postDto.getImageUrls() != null) {
                post.getImages().clear();
                for (String url : postDto.getImageUrls()) {
                    Image image = new Image();
                    image.setUrl(url);
                    image.setPost(post);
                    post.getImages().add(image);
                }
            }
            if (postDto.getLinkPaths() != null) {
                post.getLinks().clear();
                for (String url : postDto.getLinkPaths()) {
                    Link link = new Link();
                    link.setPath(url);
                    link.setPost(post);
                    post.getLinks().add(link);
                }
            }
            post.setStatus(PostStatus.EDITED);
            postRepository.update(post);
            return "Post Updated Successfully";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Error");
        }
    }

    @Override
    public String deletePost(String token, int postId)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);
            Post post = postRepository.getById(postId);
            if (post == null) throw new ClientException("Post Not Found");
            User author = post.getAuthor();
            if (!author.equals(user)) throw new ClientException("You do not have permission to update this post");
            post.setStatus(PostStatus.DELETED);
            postRepository.delete(postId);
            return "Post Deleted Successfully";
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Error");
        }
    }

    @Override
    public List<PostToReturnDto> getUserFeed(String token)
    {
        try
        {
            User user = _AuthenticationService.authenticate(token, authenticationRepository);

            Set<Integer> friendIds = user.getFriends().stream()
                    .map(User::getId)
                    .collect(java.util.stream.Collectors.toSet());

            List<Post> feedPosts = postRepository.getFeedPosts(user.getId(), friendIds);
            if (feedPosts == null) return new ArrayList<PostToReturnDto>();

            // Convert posts to DTOs
            return feedPosts.stream()
            .map(post ->
            {
                PostToReturnDto dto = new PostToReturnDto();
                dto.setPostId((long) post.getId());
                dto.setDescription(post.getDescription());
                dto.setVisability(post.getVisability().toString());
                dto.setAuthorUsername(post.getAuthor().getfName() + " " + post.getAuthor().getlName());
                dto.setCreationDate(post.getCreationDate());

                // Set image URLs
                Set<String> imageUrls = post.getImages().stream()
                        .map(Image::getUrl)
                        .collect(java.util.stream.Collectors.toSet());
                dto.setImageUrls(imageUrls);

                // Set link paths
                Set<String> linkPaths = post.getLinks().stream()
                        .map(Link::getPath)
                        .collect(java.util.stream.Collectors.toSet());
                dto.setLinkPaths(linkPaths);

                // Set likes count
                dto.setLikesCount(post.getLikes().size());

                // Set likes
                Set<LikeToReturnDto> likesOnPost = post.getLikes().stream()
                        .map(like -> {
                            LikeToReturnDto likeDto = new LikeToReturnDto();
                            likeDto.setUserId(like.getUser().getId());
                            likeDto.setUserName(like.getUser().getfName() + " " + like.getUser().getlName());
                            likeDto.setLikeType(like.getLikeType().toString());
                            return likeDto;
                        })
                        .collect(java.util.stream.Collectors.toSet());
                dto.setLikesOnPost(likesOnPost);

                // Set comments
                Set<CommentToReturnDto> comments = post.getComments().stream()
                        .map(comment -> {
                            CommentToReturnDto commentDto = new CommentToReturnDto();
                            if (comment.getParentComment() != null)
                                commentDto.setParentCommentId(comment.getParentComment().getId());
                            commentDto.setCommentId((long) comment.getId());
                            commentDto.setDescription(comment.getDescription());
                            commentDto.setAuthorId(comment.getUser().getId());
                            commentDto.setAuthorName(comment.getUser().getfName() + " " + comment.getUser().getlName());
                            commentDto.setCreationDate(comment.getCreationDate());

                            // Set image URLs
                            Set<String> imageUrl = comment.getImages().stream()
                                    .map(Image::getUrl)
                                    .collect(java.util.stream.Collectors.toSet());
                            commentDto.setImageUrls(imageUrl);

                            // Set link paths
                            Set<String> linkPath = comment.getLinks().stream()
                                    .map(Link::getPath)
                                    .collect(java.util.stream.Collectors.toSet());
                            commentDto.setLinkPaths(imageUrl);

                            // Set likes
                            Set<LikeToReturnDto> likesOnComments = comment.getLikes().stream()
                                    .map(like -> {
                                        LikeToReturnDto likeDto = new LikeToReturnDto();
                                        likeDto.setUserId(like.getUser().getId());
                                        likeDto.setUserName(like.getUser().getfName() + " " + like.getUser().getlName());
                                        likeDto.setLikeType(like.getLikeType().toString());
                                        return likeDto;
                                    })
                                    .collect(java.util.stream.Collectors.toSet());
                            commentDto.setLikesOnComment(likesOnComments);

                        return commentDto;
                        })
                        .collect(java.util.stream.Collectors.toSet());
                dto.setComments(comments);
                return dto;
            })
            .collect(java.util.stream.Collectors.toList());
        }
        catch (InternalServerException e) {
            throw new InternalServerException("Internal Server Error");
        }
    }


}
