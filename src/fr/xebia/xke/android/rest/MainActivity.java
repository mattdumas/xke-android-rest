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

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        setListAdapter(mAdapter);

        mServiceHelper = TwitterServiceHelper.getInstance(getApplicationContext());

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
        return new TweetsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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
        getLoaderManager().restartLoader(TWEETS_LOADER_ID, null, this);
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
        mAdapter.clear();
        mAdapter.addAll(tweets);
    }

}