/**
 * <chinmay keskar>, <001221409>, <keskar.c@husky.neu.edu>
 * <harshal neelkamal>, <001645951>, <neelkamal.h@husky.neu.edu>
 * <snigdha joshi>, <001602328>, <joshi.sn@husky.neu.edu>
 * <piyush sharma>, <001282198>, <sharma.pi@husky.neu.edu>
 **/


package com.csye6225.demo.bean;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Task> taskSet;

    @Column(unique = true)
    private String username;

    @Column
    private String password;


    public int getUid() {
        return uid;
    }


    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Task> getTaskSet() {
        return taskSet;
    }

    public void setTaskSet(Set<Task> taskSet) {
        this.taskSet = taskSet;
    }
}