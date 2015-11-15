package com.codepath.apps.restclienttemplate.clients;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "LGXI91lhdbmeZ39yv0CA3noLZ";
	public static final String REST_CONSUMER_SECRET = "5oTaPrzERT7okTyMhRkPgMd8rx80hXypKvDvZycg3J4JLS0EZv";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

	public static final String HOME_TIMELINE_URL = "statuses/home_timeline.json";
	public static final String USER_TIMELINE_URL = "statuses/user_timeline.json";
	public static final String MENTIONS_TIMELINE_URL = "statuses/mentions_timeline.json";
    public static final String ACCOUNT_INFO_URL = "account/verify_credentials.json";
    public static final String USER_INFO_URL = "users/show.json";
    public static final String TWEET_URL = "statuses/update.json";

	public static final int COUNT = 20;
	public static final int TIME_OUT = 4000;

    private final OAuthAsyncHttpClient clientInstance = getClient();

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        clientInstance.setTimeout(TIME_OUT);
	}

	public void getHomeTimeline(Long maxId, AsyncHttpResponseHandler httpResponseHandler) {
        String apiURL = getApiUrl(HOME_TIMELINE_URL);
        makeRequest(apiURL, maxId, httpResponseHandler);
	}

    public void createTweet(String tweetBody, AsyncHttpResponseHandler httpResponseHandler) {
        String apiURL = getApiUrl(TWEET_URL);
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        clientInstance.post(apiURL, params, httpResponseHandler);
    }

    public void getMentionsline(Long maxId, AsyncHttpResponseHandler httpResponseHandler) {
        String apiURL = getApiUrl(MENTIONS_TIMELINE_URL);
        makeRequest(apiURL, maxId, httpResponseHandler);
    }

    private void makeRequest(String apiURL, Long maxId, AsyncHttpResponseHandler httpResponseHandler) {
        Log.i(apiURL, String.valueOf(maxId));
        RequestParams params = new RequestParams();
        params.put("count", COUNT);
        if (maxId != null) {
            params.put("max_id", maxId);
        }

        clientInstance.setTimeout(TIME_OUT);
        clientInstance.get(apiURL, params, httpResponseHandler);
    }

    public void getUserTimeline(String screenName, Long maxId, AsyncHttpResponseHandler httpResponseHandler) {
        String apiURL = getApiUrl(USER_TIMELINE_URL);
        RequestParams params = new RequestParams();
        params.put("count", COUNT);

        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        if (maxId != null) {
            params.put("max_id", maxId);
        }

        clientInstance.setTimeout(TIME_OUT);
        clientInstance.get(apiURL, params, httpResponseHandler);
    }

    public void getAccountInfo(AsyncHttpResponseHandler httpResponseHandler) {
        String apiURL = getApiUrl(ACCOUNT_INFO_URL);
        clientInstance.setTimeout(TIME_OUT);
        clientInstance.get(apiURL, null, httpResponseHandler);
    }

    public void getUserInfo(String screenName, AsyncHttpResponseHandler httpResponseHandler) {
        String apiURL = getApiUrl(USER_INFO_URL);
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        clientInstance.setTimeout(TIME_OUT);
        clientInstance.get(apiURL, params, httpResponseHandler);
    }
}