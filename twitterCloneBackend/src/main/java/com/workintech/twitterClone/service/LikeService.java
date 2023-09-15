package com.workintech.twitterClone.service;

public interface LikeService {
    Long getLikeCountForTweet(int tweetId);
    boolean likeTweet(int tweetId, int memberId);
    void removeLike(int tweetId, int memberId);

}
