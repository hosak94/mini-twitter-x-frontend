package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Comment;
import com.workintech.twitterClone.entity.Tweet;
import com.workintech.twitterClone.repository.CommentRepository;
import com.workintech.twitterClone.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private TweetRepository tweetRepository;
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TweetRepository tweetRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<Comment> getCommentsForTweet(int tweetId) {
        return commentRepository.findByTweet_TweetId(tweetId);
    }

    @Override
    public Comment saveCommentForTweet(int tweetId, Comment comment) {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        if(tweet != null) {
            comment.setTweet(tweet);
            return commentRepository.save(comment);
        }else {
            return null;
        }
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }
}
