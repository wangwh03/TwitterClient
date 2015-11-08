package com.codepath.apps.restclienttemplate.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by wewang on 11/7/15.
 */
public final class RelativeTimestampParser {
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            relativeDate = relativeDate
//                    .replace(" ago", "")
//                    .replace("in ", "")
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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
