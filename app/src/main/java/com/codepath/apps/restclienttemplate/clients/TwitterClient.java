package com.codepath.apps.restclienttemplate.clients;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

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
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "LGXI91lhdbmeZ39yv0CA3noLZ";
	public static final String REST_CONSUMER_SECRET = "5oTaPrzERT7okTyMhRkPgMd8rx80hXypKvDvZycg3J4JLS0EZv";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public static final String HOME_TIMELINE_URL = "statuses/home_timeline.json";

	public static final int COUNT = 20;
    public static final int DEFAULT_COUNT = 0;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	public void getHomeTimeline(int sinceId, AsyncHttpResponseHandler httpResponseHandler) {
        Log.i("TwitterClient since id", sinceId+"");
        String apiURL = getApiUrl(HOME_TIMELINE_URL);

		RequestParams params = new RequestParams();
		params.put("count", COUNT);
		params.put("since_id", sinceId);

		getClient().get(apiURL, params, httpResponseHandler);
	}

}