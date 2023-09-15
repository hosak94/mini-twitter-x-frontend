package com.workintech.twitterClone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}
