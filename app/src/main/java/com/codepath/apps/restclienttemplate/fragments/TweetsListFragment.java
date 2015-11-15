package com.codepath.apps.restclienttemplate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.DetailedViewActivity;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.clients.TwitterClient;
import com.codepath.apps.restclienttemplate.listeners.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wewang on 11/11/15.
 */
public abstract class TweetsListFragment extends Fragment {
    protected ArrayList<Tweet> tweets;
    private TweetsArrayAdapter arrayAdapter;
    private ListView lvTweets;

    protected SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(arrayAdapter);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimelineLoadMore();
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

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimelineOnRefresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        arrayAdapter = new TweetsArrayAdapter(getActivity(), tweets);
        arrayAdapter.setOnClickProfileListener(new TweetsArrayAdapter.OnClickProfileListener() {
            @Override
            public void onProfileImageClick(String screenName) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("screenName", screenName);
                startActivity(intent);
            }
        });
    }

    public void addAll(List<Tweet> tweets) {
        arrayAdapter.addAll(tweets);
    }

    public void clearAll() {
        arrayAdapter.clear();
    }

    private void launchDetailedView(Tweet selectedItem) {
        Intent intent = new Intent(getActivity(), DetailedViewActivity.class);
        intent.putExtra("selectedTweet", selectedItem);
        startActivity(intent);
    }

    protected void populateTimelineLoadMore() {
        populateTimeline(tweets.get(tweets.size() - 1).getRemoteId() - 1, false);
    }

    protected void populateTimelineOnRefresh() {
        populateTimeline(null, true);
    }

    protected void populateTimelineOnCreate() {
        populateTimeline(null, false);
    }

    protected abstract void populateTimeline(final Long maxId, final boolean isRefresh);

    protected void handleError(Long maxId) {
        Toast.makeText(getContext(),
                getString(R.string.error_loading_tweets),
                Toast.LENGTH_LONG).show();
        loadFromCache(maxId);
    }

    protected void loadFromCache(Long maxId) {
        From select = new Select().from(Tweet.class)
                .limit(TwitterClient.COUNT);

        if (maxId != null) {
            select = select.where("remote_id <= ?", maxId);
        }

        List<Tweet> tweets = select.orderBy("timestamp").execute();

        Log.i("fetched from db", tweets.toString());
        addAll(tweets);
    }

}
