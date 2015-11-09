package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.clients.TwitterClient;
import com.codepath.apps.restclienttemplate.listeners.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.TimelineResponseParser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient twitterClient;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter arrayAdapter;
    private ListView lvTweets;
    private TextView tvError;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tvError = (TextView) findViewById(R.id.tvErrorMessage);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        arrayAdapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(arrayAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.i("timeline", "loading more with total current" + totalItemsCount);
                populateTimeline(totalItemsCount);
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                arrayAdapter.clear();
//                new Delete().from(Tweet.class).execute();
//                populateTimeline(TwitterClient.DEFAULT_COUNT);
                onRefreshTest();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        twitterClient = TwitterApplication.getRestClient();
        populateTimeline(TwitterClient.DEFAULT_COUNT);
    }

    private void onRefreshTest() {
        List<Tweet> cachedTweets = new Select().from(Tweet.class)
//                                .orderBy("timestamp ASC")
//                                .limit(TwitterClient.COUNT)
//                                .offset(totalItemsCount)
                .execute();
        List<User> users = new Select().from(User.class).execute();

        Log.i("loaded cached tweets", cachedTweets.toString());
        Log.i("loaded cached users", users.toString());
        arrayAdapter.addAll(cachedTweets);
        swipeContainer.setRefreshing(false);
    }
    private void populateTimeline(final int totalItemsCount) {
        twitterClient.getHomeTimeline(totalItemsCount + 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<Tweet> newTweets = TimelineResponseParser.createTweets(response);
                Log.i("tweets", newTweets.toString());
                arrayAdapter.addAll(newTweets);
                ActiveAndroid.beginTransaction();
                try {
                    for (Tweet tweet : newTweets) {
                        User existingUser =  new Select()
                                .from(User.class)
                                .where("remote_id = ?", tweet.getUser().getRemoteId())
                                .executeSingle();
                        if (existingUser == null) {
                            tweet.getUser().save();
                            Log.i("saved user 1", tweet.getUser().toString());
                        } else {
                            tweet.setUser(existingUser);
                        }
                        tweet.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                Log.i("saved tweets 1", newTweets.toString());

                List<Tweet> cachedTweets = new Select().from(Tweet.class)
//                                .orderBy("timestamp ASC")
//                                .limit(TwitterClient.COUNT)
//                                .offset(totalItemsCount)
                        .execute();
                Log.i("saved tweets 2", cachedTweets.toString());
                List<User> users = new Select().from(User.class).execute();
                Log.i("saved user", users.toString());
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("error loading tweets", errorResponse.toString());
                try {
                    String errorMessage = errorResponse.getJSONArray("errors").getJSONObject(0).getString("message");
                    // If some tweets are displayed already flash the error message, otherwise permanently displays error msg
                    if (tweets.size() > 0) {
                        Toast toast = Toast.makeText(TimelineActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        toast.show();
                    } else {
                        tvError.setText(errorResponse.getJSONArray("errors").getJSONObject(0).getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error loading tweets", "Cannot parse error message");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose) {
            Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
