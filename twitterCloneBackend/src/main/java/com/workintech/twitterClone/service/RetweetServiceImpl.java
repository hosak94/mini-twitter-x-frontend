package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.entity.Retweet;
import com.workintech.twitterClone.entity.Tweet;
import com.workintech.twitterClone.repository.MemberRepository;
import com.workintech.twitterClone.repository.RetweetRepository;
import com.workintech.twitterClone.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RetweetServiceImpl implements RetweetService {
    private RetweetRepository retweetRepository;
    private TweetRepository tweetRepository;
    @Autowired
    public RetweetServiceImpl(RetweetRepository retweetRepository, TweetRepository tweetRepository) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<Retweet> getRetweetsForTweet(int tweetId) {
        return retweetRepository.findByTweet_TweetId(tweetId);
    }

    @Override
    public Retweet createRetweet(int tweetId, int memberId) {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        if(tweet != null) {
            Member member = new Member();
            member.setMemberId(memberId);

            Retweet retweet = new Retweet();
            retweet.setTweet(tweet);
            retweet.setMember(member);

            return retweetRepository.save(retweet);
        }else {
            return null;
        }
    }

    @Override
    public void deleteRetweet(int retweetId) {
        retweetRepository.deleteById(retweetId);
    }

    @Override
    public Long getRetweetCountForTweet(int tweetId) {
        return retweetRepository.countByTweet_TweetId(tweetId);
    }

    @Override
    public boolean hasUserRetweetedTweet(int tweetId, int memberId) {
        return retweetRepository.hasUserRetweetedTweet(tweetId, memberId);
    }
}
