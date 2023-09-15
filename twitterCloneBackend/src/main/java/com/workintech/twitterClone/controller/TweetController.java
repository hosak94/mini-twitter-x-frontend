package com.workintech.twitterClone.controller;

import com.workintech.twitterClone.dto.TweetResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tweet")
public class TweetController {
    private TweetService tweetService;
    private UserService userService;
    private LikeService likeService;
    @Autowired
    public TweetController(TweetService tweetService, UserService userService, LikeService likeService) {
        this.tweetService = tweetService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @GetMapping("/")
    public List<TweetResponse> findAll() {
        try {
            List<Tweet> tweets = tweetService.findAll();
            return tweets.stream()
                    .map(this::mapToTweetResponse)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            throw new TweetException("An error occurred while fetching tweets.",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<TweetResponse> findById(@PathVariable int id) {
       try{
           Tweet tweet = tweetService.findById(id);
           if (tweet != null) {
               TweetResponse tweetResponse = mapToTweetResponse(tweet);
               return ResponseEntity.ok(tweetResponse);
           } else {
               return ResponseEntity.notFound().build();
           }
       }catch (Exception e) {
           throw new TweetException("Tweet with given id is not exist: "+id, HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/")
    public ResponseEntity<TweetResponse> save(@RequestBody Tweet tweet) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Member currentUser = (Member) userService.loadUserByUsername(username);
            if (currentUser != null) {
                tweet.setMember(currentUser);
                Tweet savedTweet = tweetService.save(tweet);
                TweetResponse tweetResponse = mapToTweetResponse(savedTweet);
                return ResponseEntity.ok(tweetResponse);
            } else {
                throw new TweetException("User not found.", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            throw new TweetException("An error occurred while saving the tweet.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponse> save(@RequestBody Tweet tweet, @PathVariable int id) {
        try{
            Tweet foundTweet = tweetService.findById(id);
            if (foundTweet != null) {
                tweet.setTweetId(id);
                LocalDateTime now = LocalDateTime.now();
                tweet.setCreatedAt(now);
                tweet.setMember(foundTweet.getMember());
                Tweet savedTweet = tweetService.save(tweet);
                TweetResponse tweetResponse = mapToTweetResponse(savedTweet);
                return ResponseEntity.ok(tweetResponse);
            } else {
                throw new TweetException("Tweet not found.", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            throw new TweetException("An error occurred while updating the tweet.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
       try{
           tweetService.delete(id);
           return ResponseEntity.noContent().build();
       }catch (Exception e) {
           throw new TweetException("An error occurred while deleting the tweet.", HttpStatus.BAD_REQUEST);
       }
    }

    private TweetResponse mapToTweetResponse(Tweet tweet) {
       TweetResponse tweetResponse = new TweetResponse();
       tweetResponse.setTweetId(tweet.getTweetId());
       tweetResponse.setContent(tweet.getContent());
       tweetResponse.setUsername(tweet.getMember().getUsernameForResponse());
       tweetResponse.setCreatedAt(tweet.getCreatedAt());
       int likeCount = likeService.getLikeCountForTweet(tweet.getTweetId()).intValue();
       tweetResponse.setLikeCount(likeCount);
       return tweetResponse;
    }
}
