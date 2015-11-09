package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private Date timestamp;

    public Tweet() {
        super();
    }

    public Tweet(long remoteId, String body, User user, String timestamp) {
        super();
        this.remoteId = remoteId;
        this.body = body;
        this.user = user;

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        try {
            this.timestamp = sf.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), "cannot parse datetime");
        }
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

    public Date getTimestamp() {
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
