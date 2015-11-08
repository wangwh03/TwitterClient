package com.codepath.apps.restclienttemplate.models;

/**
 * Created by wewang on 11/7/15.
 */
public class User {
    private String name;
    private long id;
    private String screenName;
    private String profileImageUrl;

    public User() {
    }

    public User(String name, long id, String screenName, String profileImageUrl) {
        this.name = name;
        this.id = id;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", screenName='" + screenName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
