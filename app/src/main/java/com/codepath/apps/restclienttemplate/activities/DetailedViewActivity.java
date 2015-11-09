package com.codepath.apps.restclienttemplate.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.customizedUIComponents.LinkifiedTextView;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailedViewActivity extends AppCompatActivity {
    private ImageView ivUserProfileImage;
    private TextView tvName;
    private TextView tvUserName;
    private LinkifiedTextView tvBody;
    private TextView tvRelativeTimeStamp;
    private TextView tvReweetCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivUserProfileImage = (ImageView) findViewById(R.id.ivDetailedProfileImage);
        tvName = (TextView) findViewById(R.id.tvDetailedName);
        tvUserName = (TextView) findViewById(R.id.tvDetailedUsername);
        tvBody = (LinkifiedTextView) findViewById(R.id.tvDetailedBody);
        tvRelativeTimeStamp = (TextView) findViewById(R.id.tvDetailedTimestamp);
        tvReweetCount = (TextView) findViewById(R.id.tvDetailedRetweetCount);

        Tweet tweet = getIntent().getParcelableExtra("selectedTweet");
        tvName.setText(tweet.getUser().getName());
        tvUserName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());

        ivUserProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivUserProfileImage);

        String twitterFormat = "MM/dd/yy, hh:mm a";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        tvRelativeTimeStamp.setText(sf.format(tweet.getTimestamp()));

        tvReweetCount.setText(String.valueOf(tweet.getRetweetCount()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_view, menu);
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
