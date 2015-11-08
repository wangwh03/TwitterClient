package com.codepath.apps.restclienttemplate.utils;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wewang on 11/7/15.
 */
public final class TimelineResponseParser {
    public static List<Tweet> createTweets(JSONArray jsonArray) {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(createTweet(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public static Tweet createTweet(JSONObject jsonObject) {
        try {
            return new Tweet(jsonObject.getLong("id"),
                    jsonObject.getString("text"),
                    createUser(jsonObject.getJSONObject("user")),
                    jsonObject.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Tweet();
    }

    public static User createUser(JSONObject jsonObject) {
        try {
            return new User(jsonObject.getString("name"),
                    jsonObject.getLong("id"),
                    jsonObject.getString("screen_name"),
                    jsonObject.getString("profile_image_url")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new User();
    }
}
