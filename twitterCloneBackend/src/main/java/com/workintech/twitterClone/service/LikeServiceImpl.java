package com.workintech.twitterClone.service;


import com.workintech.twitterClone.entity.Like;
import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.entity.Tweet;
import com.workintech.twitterClone.repository.LikeRepository;
import com.workintech.twitterClone.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    private LikeRepository likeRepository;
    private TweetRepository tweetRepository;
    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, TweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Long getLikeCountForTweet(int tweetId) {
        return likeRepository.countByTweet_TweetId(tweetId);
    }

    @Override
    public boolean likeTweet(int tweetId, int memberId) {
        boolean alreadyLiked = likeRepository.existsByTweet_TweetIdAndMember_MemberId(tweetId, memberId);
        if(alreadyLiked){
            return false;
        }else {
            Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
            Member member = new Member();
            member.setMemberId(memberId);

            if(tweet != null) {
                Like like = new Like();
                like.setTweet(tweet);
                like.setMember(member);
                likeRepository.save(like);
                return true;
            }else {
                return false;
            }

        }

    }

    @Override
    public void removeLike(int tweetId, int memberId) {
        Like like = likeRepository.findByTweet_TweetIdAndMember_MemberId(tweetId, memberId);
        if (like != null) {
            likeRepository.delete(like);
        }
    }

}
