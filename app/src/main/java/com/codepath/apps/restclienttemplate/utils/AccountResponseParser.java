package com.codepath.apps.restclienttemplate.utils;

import com.codepath.apps.restclienttemplate.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wewang on 11/8/15.
 */
public final class AccountResponseParser {

    public static User createUser(JSONObject jsonObject) {
        try {
            return new User(
                    jsonObject.getLong("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("screen_name"),
                    jsonObject.getString("profile_image_url"),
                    jsonObject.getString("description"),
                    jsonObject.getInt("follower_count"),
                    jsonObject.getInt("friends_count")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new User();
    }
}
