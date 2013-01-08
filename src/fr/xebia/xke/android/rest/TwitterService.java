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
        // FIXME: extract params (ResultReceiver)
        ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

        try {
            // FIXME: prepare HTTP request
            HttpRequestBase request = prepareRequest();

            // FIXME: execute HTTP request
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);

            // FIXME: Process HTTP response (parse & notify & persist)
            processResponse(response, resultReceiver);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error trying to build URI", e);
            // FIXME send notification with an invalid error code (i.e -1)
            resultReceiver.send(0, null);
        } catch (IOException e) {
            // FIXME send notification with an invalid error code (i.e -1)
            Log.e(TAG, "Error tying to get last tweets", e);
            resultReceiver.send(0, null);
        }
    }


    private HttpRequestBase prepareRequest() throws URISyntaxException {
        // TODO: do everything with the builder
        Uri uri = Uri.parse(TWITTER_API_ENDPOINT);

        Uri.Builder builder = uri.buildUpon();

        builder.appendQueryParameter("q", "android");

        HttpGet request = new HttpGet();
        request.setURI(new URI(builder.build().toString()));

        return request;
    }

    private void processResponse(HttpResponse response, ResultReceiver resultReceiver) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // FIXME: call parse tweets by converting entity in string (look @ EntityUtils)
            ArrayList<String> tweets = parseTweets(EntityUtils.toString(entity));

            // FIXME: persist tweets
            persistTweets(tweets);

            // FIXME: notify
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine != null ? statusLine.getStatusCode() : 0;
            notify(resultReceiver, statusCode, tweets);
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
        // FIXME: get a writable database from SQLiteOpenHelper member
        SQLiteDatabase db = mSqLiteOpenHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // FIXME: delete previous cached tweets
            db.delete(Tables.TWEETS, null, null);

            // FIXME: instanciate ContentValues object and populate it with tweets
            for (String tweet : tweets) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TweetColumns.TWEET_CONTENT, tweet);

                // FIXME: insert ContentValues in DB
                db.insert(Tables.TWEETS, null, contentValues);
            }

            db.setTransactionSuccessful();
        }
        finally {
            if (db.inTransaction()) db.endTransaction();
        }

    }

    private void notify(ResultReceiver resultReceiver, int statusCode, ArrayList<String> tweets) {
        Bundle resultData = new Bundle();
        resultData.putStringArrayList(TWEETS, tweets);
        resultReceiver.send(statusCode, resultData);
    }

}
