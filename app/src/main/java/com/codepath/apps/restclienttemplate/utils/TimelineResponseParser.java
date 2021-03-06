package com.codepath.apps.restclienttemplate.utils;

import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetType;
import com.codepath.apps.restclienttemplate.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wewang on 11/7/15.
 */
public final class TimelineResponseParser {
    public static List<Tweet> createTweets(JSONArray jsonArray, TweetType type) {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(createTweet(jsonArray.getJSONObject(i), type));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public static Tweet createTweet(JSONObject jsonObject, TweetType type) {
        try {
            Tweet retweetedOriginal = null;
            if (!jsonObject.isNull("retweeted_status")) {
                JSONObject retweetedStatus= jsonObject.getJSONObject("retweeted_status");
                retweetedOriginal = new Tweet(retweetedStatus.getLong("id"),
                        retweetedStatus.getString("text"),
                        findOrCreateUser(retweetedStatus.getJSONObject("user")),
                        retweetedStatus.getString("created_at"),
                        retweetedStatus.getInt("retweet_count"),
                        type, null);
            }

            return new Tweet(jsonObject.getLong("id"),
                    jsonObject.getString("text"),
                    findOrCreateUser(jsonObject.getJSONObject("user")),
                    jsonObject.getString("created_at"),
                    jsonObject.getInt("retweet_count"),
                    type, retweetedOriginal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Tweet();
    }

    public static User findOrCreateUser(JSONObject jsonObject) {
        try {
            long rId = jsonObject.getLong("id"); // get just the remote id
            User existingUser =
                    new Select().from(User.class).where("remote_id = ?", rId).executeSingle();
            if (existingUser != null) {
                // found and return existing
                return existingUser;
            } else {
                // create and return new user
                User user = new User(jsonObject.getLong("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("screen_name"),
                        jsonObject.getString("profile_image_url"),
                        jsonObject.getString("description"),
                        jsonObject.getInt("followers_count"),
                        jsonObject.getInt("friends_count"),
                        false
                        );
                user.save();
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new User();
    }
}
