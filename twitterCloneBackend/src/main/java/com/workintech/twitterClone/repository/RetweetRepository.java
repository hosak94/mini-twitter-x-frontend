package com.workintech.twitterClone.repository;

import com.workintech.twitterClone.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Integer> {
    List<Retweet> findByTweet_TweetId(int tweetId);
    Long countByTweet_TweetId(int tweetId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Retweet r WHERE r.tweet.tweetId = :tweetId AND r.member.memberId = :memberId")
    boolean hasUserRetweetedTweet(@Param("tweetId") int tweetId, @Param("memberId") int memberId);
}
