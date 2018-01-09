package com.android.springboard.neednetwork.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shouib on 7/9/2017.
 */

public class Need {

    public Need() {
        super();
        // TODO Auto-generated constructor stub
    }

    private String id;

    private String description;

    private List<String> mConnectionsList = new ArrayList<>();

    public String getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    private String status;

    private int targetAmount;

    private	int currentAmount;

    private String createdBy;






}

