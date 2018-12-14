/**
 * <chinmay keskar>, <001221409>, <keskar.c@husky.neu.edu>
 * <harshal neelkamal>, <001645951>, <neelkamal.h@husky.neu.edu>
 * <snigdha joshi>, <001602328>, <joshi.sn@husky.neu.edu>
 * <piyush sharma>, <001282198>, <sharma.pi@husky.neu.edu>
 **/

package com.csye6225.demo.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.csye6225.demo.auth.BCryptPasswordEncoderBean;
import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.UserRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class HomeController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoderBean bCry;
  private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String welcome(HttpServletRequest request) {

    System.err.print(""+request.getContextPath());
    JsonObject jsonObject = new JsonObject();

    if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
      jsonObject.addProperty("message", "you are not logged in!!!");
    } else {
      jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
    }

    return jsonObject.toString();
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String test() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /test");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/testPost", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String testPost() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /testPost");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String createAccount(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletRequest request){
    JsonObject object = new JsonObject();
    User user = userRepository.findByUsername(username);

    if(user != null){
      object.addProperty("message: ","User Name "+username+" Already Taken");
      return object.toString();
    }
    password = password+password.hashCode();
    String encPass = bCry.bCryptPasswordEncoder().encode(password);
    User u = new User();
    u.setUsername(username);
    u.setPassword(encPass);
    userRepository.save(u);

    object.addProperty(username,password);

     return object.toString();
  }

  @RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String passwordReset(@RequestParam(value = "username") String username){
    JsonObject object = new JsonObject();
    User user = userRepository.findByUsername(username);
    object.addProperty("message: ","A mail with a reset link has been sent to: "+ username);

    if(user == null){
      return object.toString();
    }

    AmazonSNS sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    CreateTopicResult topicResult = sns.createTopic("SESTopic");

    String arn = topicResult.getTopicArn();//obj.get("TopicArn").getAsString();
    PublishRequest publishRequest = new PublishRequest(arn,""+username+","+user.getUid());
    PublishResult publishResult = sns.publish(publishRequest);

    return object.toString();
  }


}