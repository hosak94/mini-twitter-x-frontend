package com.workintech.twitterClone.controller;

import com.workintech.twitterClone.entity.Like;
import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.entity.Tweet;
import com.workintech.twitterClone.exceptions.TweetException;
import com.workintech.twitterClone.service.LikeService;
import com.workintech.twitterClone.service.TweetService;
import com.workintech.twitterClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweet/like")
public class LikeController {
    private LikeService likeService;
    private TweetService tweetService;
    private UserService userService;
    @Autowired
    public LikeController(LikeService likeService, TweetService tweetService, UserService userService) {
        this.likeService = likeService;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping("/{tweetId}")
    public ResponseEntity<Void> likeTweet (@PathVariable int tweetId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Member currentUser = (Member) userService.loadUserByUsername(username);
            if(currentUser != null) {
                Tweet tweet = tweetService.findById(tweetId);
                if(tweet != null) {
                    likeService.likeTweet(tweetId,currentUser.getMemberId());
                    return ResponseEntity.ok().build();
                }else {
                    throw new TweetException("Tweet not found.", HttpStatus.NOT_FOUND);
                }
            }else {
                throw new TweetException("User not found.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new TweetException("An error occurred while liking the tweet.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> unlikeTweet(@PathVariable int tweetId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Member currentUser = (Member) userService.loadUserByUsername(username);
            if(currentUser != null) {
                Tweet tweet = tweetService.findById(tweetId);
                if(tweet != null) {
                    likeService.removeLike(tweetId,currentUser.getMemberId());
                    return ResponseEntity.ok().build();
                }else {
                    throw new TweetException("Tweet not found.", HttpStatus.NOT_FOUND);
                }
            }else {
                throw new TweetException("User not found.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new TweetException("An error occurred while unliking the tweet.", HttpStatus.BAD_REQUEST);
        }
    }
}
