package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.clients.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.TimelineResponseParser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wewang on 11/13/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient twitterClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        populateTimelineOnCreate();
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    protected void populateTimeline(final Long maxId, final boolean isRefresh) {
        String screenName = getArguments().getString("screenName");
        twitterClient.getUserTimeline(screenName, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (isRefresh) {
                    clearAll();
                    new Delete().from(Tweet.class).execute();
                }

                List<Tweet> newTweets = TimelineResponseParser.createTweets(response);
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

                    toastError("Cannot retrieve Tweets at this time. Please try again later.");
                    loadFromCache(maxId);
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error loading tweets", "Cannot parse error message");
                }
            }
        });
    }

    private void toastError(String errorMessage) {
        Toast.makeText(getActivity(),
                errorMessage,
                Toast.LENGTH_LONG).show();
    }
}
