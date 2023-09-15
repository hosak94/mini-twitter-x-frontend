package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Tweet;
import com.workintech.twitterClone.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImpl implements TweetService{
    private TweetRepository tweetRepository;
    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet findById(int id) {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if(tweet.isPresent()){
            return tweet.get();
        }
        //throw Exception
        return null;
    }

    @Override
    public Tweet save(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet delete(int id) {
        Tweet tweet = findById(id);
        tweetRepository.delete(tweet);
        return tweet;
    }
}
