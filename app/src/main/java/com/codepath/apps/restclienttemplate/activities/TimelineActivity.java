package com.codepath.apps.restclienttemplate.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient twitterClient;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter arrayAdapter;
    private ListView lvTweets;
    private TextView tvError;

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
                Log.i("timeline", "loading more with totalcurrent" + totalItemsCount);
                populateTimeline(totalItemsCount);
                return true;
            }
        });

        twitterClient = TwitterApplication.getRestClient();
        populateTimeline(TwitterClient.DEFAULT_COUNT);
    }

    private void populateTimeline(int totalItemsCount) {
        twitterClient.getHomeTimeline(totalItemsCount + 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                arrayAdapter.addAll(TimelineResponseParser.createTweets(response));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
