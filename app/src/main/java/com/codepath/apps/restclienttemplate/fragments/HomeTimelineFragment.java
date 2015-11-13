package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
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
 * Created by wewang on 11/11/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient twitterClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        populateTimeline(TwitterClient.DEFAULT_COUNT);
    }

    protected void populateTimeline(final int totalItemsCount) {
        populateTimeline(totalItemsCount, false);
    }

    protected void populateTimeline(final int totalItemsCount, final boolean isRefresh) {
        twitterClient.getHomeTimeline(totalItemsCount + 1, new JsonHttpResponseHandler() {
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
                    loadFromCache(totalItemsCount);
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error loading tweets", "Cannot parse error message");
                }
            }
        });
    }

    private void toastError(String errorMessage) {
        Toast.makeText(getContext(),
                errorMessage,
                Toast.LENGTH_LONG).show();
    }

    private void loadFromCache(int totalItemCount) {
        List<Tweet> tweets = new Select().from(Tweet.class)
                .limit(TwitterClient.COUNT)
                .offset(totalItemCount + 1)
                .orderBy("timestamp")
                .execute();
        Log.i("fetched from db", tweets.toString());
        addAll(tweets);
    }

}
