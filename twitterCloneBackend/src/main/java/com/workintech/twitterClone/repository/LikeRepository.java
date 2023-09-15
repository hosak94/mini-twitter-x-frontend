package com.workintech.twitterClone.repository;

import com.workintech.twitterClone.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    Long countByTweet_TweetId(int tweetId);

    Like findByTweet_TweetIdAndMember_MemberId(int tweetId, int memberId);
    boolean existsByTweet_TweetIdAndMember_MemberId(int tweetId, int memberId);
}
