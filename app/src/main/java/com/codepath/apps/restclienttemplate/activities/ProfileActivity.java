package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.clients.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.TimelineResponseParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String screenName = getIntent().getStringExtra("screenName");
        client = TwitterApplication.getRestClient();
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = TimelineResponseParser.findOrCreateUser(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String errorMessage = "Error loading user info! ";
                if (errorResponse != null) {
                    errorMessage = errorResponse.toString();
                }
                Log.e(this.getClass().toString(), errorMessage);
                Toast.makeText(ProfileActivity.this, getString(R.string.error_loading_user_info), Toast.LENGTH_LONG).show();
                loadUserFromCache(screenName);
            }
        });

        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvUserName);
        TextView tvTagline = (TextView) findViewById(R.id.tvUserTagLine);
        TextView tvFollower = (TextView) findViewById(R.id.tvFollower);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivUserProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        if (user.getFollowersCount() == 1) {
            tvFollower.setText(getString(R.string.follower).replace("{:count}", "1"));
        } else {
            tvFollower.setText(getString(R.string.followers).replace("{:count}", String.valueOf(user.getFollowersCount())));
        }
        tvFollowing.setText(getString(R.string.following).replace("{:count}", String.valueOf(user.getFollowingsCount())));

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    private void loadUserFromCache(String screenName) {
        user = new Select().from(User.class).where("screen_name = ?", screenName).executeSingle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
