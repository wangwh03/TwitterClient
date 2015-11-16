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
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followers_count")
    private int followersCount;
    @Column(name = "followings_count")
    private int followingsCount;
    @Column(name = "is_owner")
    private boolean isOwner;

    public User() {
        super();
    }

    public User(long remoteId, String name, String screenName, String profileImageUrl,
                String tagline, int followersCount, int followingsCount, boolean isOwner) {
        this.remoteId = remoteId;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.tagline = tagline;
        this.followersCount = followersCount;
        this.followingsCount = followingsCount;
        this.isOwner = isOwner;
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

    public boolean isOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
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
                ", isOwner=" + isOwner +
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
        dest.writeByte(isOwner ? (byte) 1 : (byte) 0);
    }

    protected User(Parcel in) {
        this.remoteId = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagline = in.readString();
        this.followersCount = in.readInt();
        this.followingsCount = in.readInt();
        this.isOwner = in.readByte() != 0;
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
