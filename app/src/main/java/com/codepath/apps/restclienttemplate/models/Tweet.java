package com.codepath.apps.restclienttemplate.models;

import java.util.Date;

/**
 * Created by wewang on 11/7/15.
 */
public class Tweet {

    private long id;
    private  String body;
    private User user;
    private String timestamp;

    public Tweet() {
    }

    public Tweet(long id, String body, User user, String timestamp) {
        this.id = id;
        this.body = body;
        this.user = user;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", user=" + user +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
