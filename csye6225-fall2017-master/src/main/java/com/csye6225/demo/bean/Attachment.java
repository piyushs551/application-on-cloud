package com.csye6225.demo.bean;

import javax.persistence.*;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int attID;

    @Column
    private String path;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name="taskID")
    private Task task;

    public int getPathID() {
        return attID;
    }

    public void setPathID(int attID) {
        this.attID = attID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
