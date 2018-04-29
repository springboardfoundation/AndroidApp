package com.android.springboard.neednetwork.models;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Shouib on 7/9/2017.
 */

public class Need {

    public Need() {
        super();
    }

    private String id;
    private String description;
    private Date targetDate; // days
    private String location;
    private String createdBy;
    private List<String> users;
    private Set<String> otherUsers;
    private String goal;

    public String getId() {
        return id;
    }

    public String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public Set<String> getOtherUsers() {
        return otherUsers;
    }

    public void setOtherUsers(Set<String> otherUsers) {
        this.otherUsers = otherUsers;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}

