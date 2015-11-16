package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
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
public class Tweet extends Model implements Parcelable {
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long remoteId;
    @Column(name = "Body")
    private  String body;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "Timestamp")
    private Date timestamp;
    @Column(name = "retweet_count")
    private int retweetCount;
    @Column(name = "type")
    private TweetType type;

    public Tweet() {
        super();
    }

    public Tweet(long remoteId, String body, User user, String timestamp, int retweetCount, TweetType tweetType) {
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

        this.retweetCount = retweetCount;
        this.type = tweetType;
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

    public int getRetweetCount() {
        return retweetCount;
    }

    public TweetType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "remoteId=" + remoteId +
                ", body='" + body + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", retweetCount=" + retweetCount +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.remoteId);
        dest.writeString(this.body);
        dest.writeParcelable(this.user, 0);
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1);
        dest.writeInt(this.retweetCount);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected Tweet(Parcel in) {
        this.remoteId = in.readLong();
        this.body = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
        this.retweetCount = in.readInt();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : TweetType.values()[tmpType];
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
