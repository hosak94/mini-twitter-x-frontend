package com.workintech.twitterClone.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "retweets",schema = "public")
public class Retweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retweet_id")
    private int retweetId;

    @ManyToOne
    @JoinColumn(name = "member_id",referencedColumnName = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "tweet_id",referencedColumnName = "tweet_id")
    private Tweet tweet;

}
