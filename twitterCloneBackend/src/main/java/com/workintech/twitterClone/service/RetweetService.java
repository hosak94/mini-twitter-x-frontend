package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Retweet;

import java.util.List;

public interface RetweetService {
    List<Retweet> getRetweetsForTweet(int tweetId);
    Retweet createRetweet(int tweetId, int memberId);
    void deleteRetweet(int retweetId);
    Long getRetweetCountForTweet(int tweetId);
    boolean hasUserRetweetedTweet(int tweetId, int memberId);
}
