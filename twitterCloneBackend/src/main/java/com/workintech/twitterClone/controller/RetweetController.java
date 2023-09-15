package com.workintech.twitterClone.controller;

import com.workintech.twitterClone.dto.RetweetResponse;
import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.entity.Retweet;
import com.workintech.twitterClone.exceptions.TweetException;
import com.workintech.twitterClone.service.RetweetService;
import com.workintech.twitterClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tweet/retweet")
public class RetweetController {
    private RetweetService retweetService;
    private UserService userService;
    @Autowired
    public RetweetController(RetweetService retweetService, UserService userService) {
        this.retweetService = retweetService;
        this.userService = userService;
    }

    @GetMapping("/{tweetId}")
    public ResponseEntity<List<RetweetResponse>> getRetweetsForTweet(@PathVariable int tweetId) {
        try {
            List<Retweet> retweets = retweetService.getRetweetsForTweet(tweetId);
            List<RetweetResponse> responses = retweets.stream()
                    .map(this::mapToRetweetResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        }catch (Exception e) {
            throw new TweetException("An error occurred while fetching retweets.", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/tweet/{tweetId}/count")
    public ResponseEntity<Long> getRetweetCountForTweet(@PathVariable int tweetId) {
       try{
           Long retweetCount = retweetService.getRetweetCountForTweet(tweetId);
           return ResponseEntity.ok(retweetCount);
       }catch (Exception e) {
           throw new TweetException("An error occurred while fetching retweet count.", HttpStatus.BAD_REQUEST);
       }
    }
    @PostMapping("/{tweetId}")
    public ResponseEntity<RetweetResponse> createRetweet(@PathVariable int tweetId) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                Member member = (Member) userService.loadUserByUsername(username);
                if(member != null) {
                    int memberId = member.getMemberId();
                    boolean hasRetweeted = retweetService.hasUserRetweetedTweet(tweetId, memberId);
                    if (!hasRetweeted) {
                        Retweet retweet = retweetService.createRetweet(tweetId, memberId);
                        if (retweet != null) {
                            RetweetResponse response = mapToRetweetResponse(retweet);
                            return ResponseEntity.ok(response);
                        }
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e) {
            throw new TweetException("An error occurred while creating a retweet.", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{retweetId}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable int retweetId) {
       try{
           retweetService.deleteRetweet(retweetId);
           return ResponseEntity.noContent().build();
       }catch (Exception e) {
           throw new TweetException("An error occurred while deleting the retweet.", HttpStatus.BAD_REQUEST);
       }
    }


    private RetweetResponse mapToRetweetResponse(Retweet retweet) {

        RetweetResponse response = new RetweetResponse();
        response.setRetweetId(retweet.getRetweetId());
        response.setTweetId(retweet.getTweet().getTweetId());

        if (retweet.getMember() != null) {
            response.setMemberId(retweet.getMember().getMemberId());
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                Member member = (Member) userService.loadUserByUsername(username);
                if (member != null) {
                    response.setMemberId(member.getMemberId());
                }
            }
        }

        return response;
    }
}
