/**
 * <chinmay keskar>, <001221409>, <keskar.c@husky.neu.edu>
 * <harshal neelkamal>, <001645951>, <neelkamal.h@husky.neu.edu>
 * <snigdha joshi>, <001602328>, <joshi.sn@husky.neu.edu>
 * <piyush sharma>, <001282198>, <sharma.pi@husky.neu.edu>
 **/

package com.csye6225.demo.auth;


import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    UserRepository rep;

    @Autowired
    private BCryptPasswordEncoderBean bCry;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        GrantedAuthority granted = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_User";
            }
        };
        ArrayList<GrantedAuthority> roleList = new ArrayList<GrantedAuthority>();
        roleList.add(granted);
        User user = rep.findByUsername(name);
        if(user == null){
            return null;
        }
        boolean match = bCry.bCryptPasswordEncoder().matches(password+password.hashCode(),user.getPassword());
        if(match){
            return new UsernamePasswordAuthenticationToken(name,password,new ArrayDeque<>());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}