package com.workintech.twitterClone.service;

import com.workintech.twitterClone.dto.LoginResponse;
import com.workintech.twitterClone.entity.Member;
import com.workintech.twitterClone.entity.Role;
import com.workintech.twitterClone.repository.MemberRepository;
import com.workintech.twitterClone.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public AuthenticationService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Member register(String email, String password, String username, String firstName, String lastName) {

        Optional<Member> foundMember = memberRepository.findMemberByEmail(email);
        if(foundMember.isPresent()) {
            //throw exception
            return null;
        }


        String encodedPassword = passwordEncoder.encode(password);
        Role memberrole = roleRepository.findByAuthority("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(memberrole);

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setUserName(username);
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setAuthorties(roles);
        return memberRepository.save(member);

    }

    public LoginResponse login(String email, String password){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password));

            String token = tokenService.generateJwtToken(authentication);
            return new LoginResponse(token);
        }catch (Exception ex){
            ex.printStackTrace();
            return new LoginResponse("");
        }
    }
}
