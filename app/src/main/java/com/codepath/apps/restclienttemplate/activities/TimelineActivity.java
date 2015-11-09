package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        arrayAdapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(arrayAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(totalItemsCount);
                return true;
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("clicked", "clicked tweet");
                Tweet selectedItem = (Tweet) lvTweets.getItemAtPosition(position);
                launchDetailedView(selectedItem);
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(TwitterClient.DEFAULT_COUNT, true);
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

    private void launchDetailedView(Tweet selectedItem) {
        Intent intent = new Intent(TimelineActivity.this, DetailedViewActivity.class);
        intent.putExtra("selectedTweet", selectedItem);
        startActivity(intent);
    }

    private void populateTimeline(final int totalItemsCount) {
        populateTimeline(totalItemsCount, false);
    }

    private void populateTimeline(final int totalItemsCount, final boolean isRefresh) {
        twitterClient.getHomeTimeline(totalItemsCount + 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (isRefresh) {
                    arrayAdapter.clear();
                    new Delete().from(Tweet.class).execute();
                }

                List<Tweet> newTweets = TimelineResponseParser.createTweets(response);
                arrayAdapter.addAll(newTweets);
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
        Toast.makeText(TimelineActivity.this,
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
        arrayAdapter.addAll(tweets);
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
