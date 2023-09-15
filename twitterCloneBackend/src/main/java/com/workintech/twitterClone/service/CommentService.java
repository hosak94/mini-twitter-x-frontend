package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsForTweet(int tweetId);
    Comment saveCommentForTweet(int tweetId, Comment comment);
    void deleteComment(int commentId);
}
