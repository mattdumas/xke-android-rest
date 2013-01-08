package fr.xebia.xke.android.rest;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static fr.xebia.xke.android.rest.DatabaseHelper.TweetColumns;
import static fr.xebia.xke.android.rest.TwitterServiceHelper.TwitterEventListener;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>, TwitterEventListener {

    private static final int TWEETS_LOADER_ID = 1;

    private ArrayAdapter<String> mAdapter;
    private TwitterServiceHelper mServiceHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Button refreshBtn = (Button) findViewById(R.id.refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshTweets();
            }
        });


        // FIXME: initialize adapter with a simple default layout (R.layout.simple_list_item_1)
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // FIXME: setListAdapter
        setListAdapter(mAdapter);

        // FIXME: get the unique instance of TwitterSearchHelper
        mServiceHelper = TwitterServiceHelper.getInstance(getApplicationContext());

        // FIXME: init Loader with getLoaderManager() (args parameter is not needed)
        getLoaderManager().initLoader(TWEETS_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mServiceHelper.addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mServiceHelper.removeListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // FIXME: returns new TweetsLoader
        return new TweetsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // FIXME: check if cursor is null, if it is, call refreshTweets, else, extract tweets from cursor and call setTweets
        if (cursor == null || !cursor.moveToFirst()) {
            refreshTweets();
        } else {
            ArrayList<String> tweets = new ArrayList<String>();

            tweets.add(cursor.getString(cursor.getColumnIndex(TweetColumns.TWEET_CONTENT)));

            while (cursor.moveToNext()) {
                String tweet = cursor.getString(cursor.getColumnIndex(TweetColumns.TWEET_CONTENT));
                tweets.add(tweet);
            }

            setTweets(tweets);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // no op
    }


    @Override
    public void onNewTweets(ArrayList<String> tweets) {
        // For loader version
        getLoaderManager().restartLoader(TWEETS_LOADER_ID, null, this);

        // For basic version
        // setTweets(tweets);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "An error occured !", Toast.LENGTH_SHORT).show();
    }


    private void refreshTweets() {
        Toast.makeText(this, "Refreshing tweets", Toast.LENGTH_SHORT).show();
        mServiceHelper.refreshTweets();
    }


    private void setTweets(ArrayList<String> tweets) {
        // FIXME: populate mAdapter with tweets
        mAdapter.clear();
        mAdapter.addAll(tweets);
    }

}