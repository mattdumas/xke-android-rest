package fr.xebia.xke.android.rest;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static fr.xebia.xke.android.rest.DatabaseHelper.Tables;
import static fr.xebia.xke.android.rest.DatabaseHelper.TweetColumns;


public class TwitterService extends IntentService {

    private static final String TAG = TwitterService.class.getName();

    private static final String TWITTER_API_ENDPOINT = "http://search.twitter.com/search.json";

    public static final String EXTRA_RESULT_RECEIVER = "fr.xebia.xke.android.rest.EXTRA_RESULT_RECEIVER";
    public static final String TWEETS = "fr.xebia.xke.android.rest.TWEETS";


    private SQLiteOpenHelper mSqLiteOpenHelper;


    public TwitterService() {
        super(TAG);
        mSqLiteOpenHelper = new DatabaseHelper(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // FIXME: 2.3.1 - Extract ResultReceiver

        try {
            // FIXME: 2.3.2
            HttpRequestBase request = prepareRequest();

            // FIXME: 2.3.3

            // FIXME: 2.3.4
            processResponse(null, null);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error trying to build URI", e);
            // FIXME: send notification with an invalid error code (i.e -1)
        } catch (IOException e) {
            // FIXME: send notification with an invalid error code (i.e -1)
            Log.e(TAG, "Error tying to get last tweets", e);
        }
    }


    private HttpRequestBase prepareRequest() throws URISyntaxException {
        // FIXME: 2.3.2

        return null;
    }

    private void processResponse(HttpResponse response, ResultReceiver resultReceiver) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            ArrayList<String> tweets = parseTweets(EntityUtils.toString(entity));

            persistTweets(tweets);

            // FIXME: 2.3.4
            // Put tweets data into a Bundle

            // Get HTTP code status with response status line

            // Send results to ResultReceiver

        }
    }

    private ArrayList<String> parseTweets(String json) {
        ArrayList<String> tweetList = new ArrayList<String>();

        try {
            JSONObject tweetsWrapper = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray tweets = tweetsWrapper.getJSONArray("results");

            for (int i = 0; i < tweets.length(); ++i) {
                JSONObject tweet = tweets.getJSONObject(i);
                tweetList.add(tweet.getString("text"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON.", e);
        }

        return tweetList;
    }

    private void persistTweets(ArrayList<String> tweets) {
        // FIXME: 3.6

        // Get a writable DB by using SQLiteOpenHelper member

        // Start a transaction

        // Delete previous tweets

        // Insert new tweets

        // Set transaction successful

    }


}
