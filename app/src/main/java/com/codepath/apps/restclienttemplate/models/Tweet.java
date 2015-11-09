package com.codepath.apps.restclienttemplate.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by wewang on 11/7/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model {
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long remoteId;
    @Column(name = "Body")
    private  String body;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "Timestamp")
    private String timestamp;

    public Tweet() {
        super();
    }

    public Tweet(long remoteId, String body, User user, String timestamp) {
        super();
        this.remoteId = remoteId;
        this.body = body;
        this.user = user;
        this.timestamp = timestamp;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + getId() +
                "remoteId=" + remoteId +
                ", body='" + body + '\'' +
                ", user=" + user +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
