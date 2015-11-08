package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.clients.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.AccountResponseParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends AppCompatActivity {
    private ImageView ivUserProfileImage;
    private TextView tvUserName;
    private TextView tvName;
    private EditText etBody;
    private TwitterClient twitterClient;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        ivUserProfileImage = (ImageView) findViewById(R.id.ivComposeUserProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvComposeUsername);
        tvName = (TextView) findViewById(R.id.tvComposeName);
        etBody = (EditText) findViewById(R.id.etTweet);

        twitterClient = TwitterApplication.getRestClient();
        twitterClient.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = AccountResponseParser.createUser(response);
                tvUserName.setText("@" + user.getScreenName());
                tvName.setText(user.getName());
                ivUserProfileImage.setImageResource(android.R.color.transparent);
                Picasso.with(ComposeActivity.this).load(user.getProfileImageUrl()).into(ivUserProfileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                showToastError(errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_tweet) {
            twitterClient.createTweet(etBody.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // Go back to home page
                    Log.d("compose", "success");
                    Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    showToastError(errorResponse.toString());
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToastError(String errorMessage) {
        Toast toast = Toast.makeText(ComposeActivity.this,
                errorMessage,
                Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        toast.show();
        Log.e(this.getClass().toString(), errorMessage);
    }

}
