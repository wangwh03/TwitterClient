package com.codepath.apps.restclienttemplate.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wewang on 11/7/15.
 */
public final class RelativeTimestampParser {
    public static String getRelativeTimeAgo(Date date) {
        String relativeDate = "";
        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        relativeDate = relativeDate
                .replace(" ago", "")
                .replace(" seconds", "s")
                .replace(" second", "s")
                .replace(" minutes", "m")
                .replace(" minute", "m")
                .replace(" hours", "h")
                .replace(" hour", "h")
                .replace(" days", "d")
                .replace(" day", "d")
                .replace(" years", "y")
                .replace(" year", "y");

        return relativeDate;
    }
}
