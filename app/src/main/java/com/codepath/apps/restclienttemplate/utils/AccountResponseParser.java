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
