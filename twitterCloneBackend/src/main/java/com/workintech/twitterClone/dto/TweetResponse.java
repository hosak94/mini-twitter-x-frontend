package com.workintech.twitterClone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponse {
    private int tweetId;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private int likeCount;

}
