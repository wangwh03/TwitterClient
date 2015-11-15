package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by wewang on 11/7/15.
 */
@Table(name = "Users")
public class User extends Model implements Parcelable {
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long remoteId;
    @Column(name = "Name")
    private String name;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followersCount")
    private int followersCount;
    @Column(name = "followingsCount")
    private int followingsCount;

    public User() {
        super();
    }

    public User(long remoteId, String name, String screenName, String profileImageUrl, String tagline, int followersCount, int followingsCount) {
        this.remoteId = remoteId;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.tagline = tagline;
        this.followersCount = followersCount;
        this.followingsCount = followingsCount;
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

    public List<Tweet> getTweets() {
        return getMany(Tweet.class, "Tweet");
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "remoteId=" + remoteId +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", tagline='" + tagline + '\'' +
                ", followersCount=" + followersCount +
                ", followingsCount=" + followingsCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.remoteId);
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.tagline);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingsCount);
    }

    protected User(Parcel in) {
        this.remoteId = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagline = in.readString();
        this.followersCount = in.readInt();
        this.followingsCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
