package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.clients.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetType;
import com.codepath.apps.restclienttemplate.utils.TimelineResponseParser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wewang on 11/11/15.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient twitterClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        populateTimelineOnCreate();
    }

    protected void populateTimeline(final Long maxId, final boolean isRefresh) {
        if (!twitterClient.isNetworkAvailable()) {
            handleError(maxId, TweetType.MENTION_TIMELINE);
            if (swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
        }
        twitterClient.getMentionsline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (isRefresh) {
                    clearAll();
                    new Delete().from(Tweet.class).execute();
                }

                List<Tweet> newTweets = TimelineResponseParser.createTweets(response, TweetType.MENTION_TIMELINE);
                addAll(newTweets);
                ActiveAndroid.beginTransaction();
                try {
                    for (Tweet tweet : newTweets) {
                        tweet.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    String errorMessage = "Error loading tweets! ";
                    if (errorResponse != null) {
                        errorMessage = errorResponse.getJSONArray("errors").getJSONObject(0).getString("message");
                    }
                    Log.e(this.getClass().toString(), errorMessage);

                    handleError(maxId, TweetType.MENTION_TIMELINE);
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error loading tweets", "Cannot parse error message");
                }
            }
        });
    }

}