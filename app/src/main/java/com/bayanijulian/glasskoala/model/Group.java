package com.bayanijulian.glasskoala.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String name;
    private int points;
    private List<String> userIds = new ArrayList<>();

    public Group() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public void addUser(String userId) {
        userIds.add(userId);
    }
}
