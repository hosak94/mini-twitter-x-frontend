package com.workintech.twitterClone.service;

import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private MemberRepository memberRepository;
    @Autowired
    public UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User is not valid"));
    }
    public int findMemberIdByUsername(String username) {
        Member member = memberRepository.findMemberByEmail(username)
                .orElse(null);
        return member != null ? member.getMemberId() : -1;
    }

}
