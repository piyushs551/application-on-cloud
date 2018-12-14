/**
 * <chinmay keskar>, <001221409>, <keskar.c@husky.neu.edu>
 * <harshal neelkamal>, <001645951>, <neelkamal.h@husky.neu.edu>
 * <snigdha joshi>, <001602328>, <joshi.sn@husky.neu.edu>
 * <piyush sharma>, <001282198>, <sharma.pi@husky.neu.edu>
 **/

package com.csye6225.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private BasicAuthEntryPoint basicAuthEntryPoint;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;


  @Autowired
  private CustomAuthenticationProvider authProvider;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.authenticationProvider(authProvider);
  }

  protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/*").authenticated()
            .and()
            .httpBasic()
            .authenticationEntryPoint(basicAuthEntryPoint);
  }

  public void configure(WebSecurity web) throws  Exception{
    web.ignoring().antMatchers("/user/register");
    web.ignoring().antMatchers("/forgot-password");
  }

  @Bean
  public HttpSessionStrategy httpSessionStrategy() {
    return new HeaderHttpSessionStrategy();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(User.withUsername("user").password(bCryptPasswordEncoder.encode("password")).roles("USER").build());
    return manager;
  }
}
