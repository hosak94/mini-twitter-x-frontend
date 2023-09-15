package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> findAll();
    Tweet findById(int id);
    Tweet save(Tweet tweet);
    Tweet delete(int id);
}
