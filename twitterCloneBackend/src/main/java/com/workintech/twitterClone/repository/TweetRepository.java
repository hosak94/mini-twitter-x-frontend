package com.workintech.twitterClone.repository;

import com.workintech.twitterClone.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet,Integer> {
}
