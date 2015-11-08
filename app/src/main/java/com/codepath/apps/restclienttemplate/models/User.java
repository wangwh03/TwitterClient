package com.codepath.apps.restclienttemplate.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by wewang on 11/7/15.
 */
@Table(name = "Users")
public class User extends Model {
    @Column(name = "remote_id", unique = true)
    private long remoteId;
    @Column(name = "name")
    private String name;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    public User() {
        super();
    }

    public User(String name, long remoteId, String screenName, String profileImageUrl) {
        super();
        this.name = name;
        this.remoteId = remoteId;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public String getName() {
        return name;
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
                ", remoteId=" + remoteId +
                ", screenName='" + screenName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
