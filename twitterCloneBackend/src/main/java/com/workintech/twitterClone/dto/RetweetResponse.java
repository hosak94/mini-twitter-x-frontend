package com.workintech.twitterClone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetweetResponse {
    private int retweetId;
    private int tweetId;
    private int memberId;
}
