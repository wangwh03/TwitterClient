package com.codepath.apps.restclienttemplate.utils;

import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.models.User;

/**
 * Created by wewang on 11/16/15.
 */
public final class OwnerHelper {
    private static User owner  = null;
    public static User fetchCurrentOwner() {
        if (owner == null) {
            owner = new Select().from(User.class).where("is_owner = ?", true).executeSingle();
        }
        return owner;
    }
}
