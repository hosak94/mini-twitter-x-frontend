package com.workintech.twitterClone.controller;

import com.workintech.twitterClone.dto.CommentResponse;
import com.workintech.twitterClone.entity.Comment;
import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.entity.Tweet;
import com.workintech.twitterClone.exceptions.TweetException;
import com.workintech.twitterClone.service.CommentService;
import com.workintech.twitterClone.service.TweetService;
import com.workintech.twitterClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tweet/reply")
public class CommentController {
    private CommentService commentService;
    private UserService userService;
   private TweetService tweetService;
    @Autowired
    public CommentController(CommentService commentService, UserService userService, TweetService tweetService) {
        this.commentService = commentService;
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @GetMapping("/{tweetId}")
    public ResponseEntity<List<CommentResponse>> getCommentsForTweet(@PathVariable int tweetId) {
        try {
            List<Comment> comments = commentService.getCommentsForTweet(tweetId);
            List<CommentResponse> responses = comments.stream()
                    .map(this::mapToCommentResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            throw new TweetException("An error occurred while fetching comments for the tweet.", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{tweetId}")
    public ResponseEntity<CommentResponse> createComment(@RequestBody Comment comment,@PathVariable int tweetId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                Member member = (Member) userService.loadUserByUsername(username);
                LocalDateTime now = LocalDateTime.now();
                if (member != null) {
                    Tweet tweet = tweetService.findById(tweetId);
                    if (tweet != null) {
                        comment.setMember(member);
                        comment.setTweet(tweet);
                        comment.setCreatedAt(now);
                        Comment createdComment = commentService.saveCommentForTweet(tweetId, comment);
                        CommentResponse response = mapToCommentResponse(createdComment);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    }
                }
            }
            throw new TweetException("Comment creation failed.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new TweetException("An error occurred while creating a comment.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{tweetId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int tweetId,@PathVariable int commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new TweetException("An error occurred while deleting the comment.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setTweetId(comment.getTweet().getTweetId());
        response.setContent(comment.getContent());
        response.setUsername(comment.getMember().getUsernameForResponse());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }

}
