/**
 * <chinmay keskar>, <001221409>, <keskar.c@husky.neu.edu>
 * <harshal neelkamal>, <001645951>, <neelkamal.h@husky.neu.edu>
 * <snigdha joshi>, <001602328>, <joshi.sn@husky.neu.edu>
 * <piyush sharma>, <001282198>, <sharma.pi@husky.neu.edu>
 **/

package com.csye6225.demo.bean;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
public class Task {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")

    private UUID taskID;

    @Column(length = 4096)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<Attachment> attachmentSet;


    public UUID getTaskID() {
        return taskID;
    }

    public void setTaskID(UUID taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Attachment> getAttachmentSet() {
        return attachmentSet;
    }

    public void setAttachmentSet(Set<Attachment> attachmentSet) {
        this.attachmentSet = attachmentSet;
    }
}