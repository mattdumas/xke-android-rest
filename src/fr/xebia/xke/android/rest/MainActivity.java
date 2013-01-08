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


public class MainActivity extends ListActivity implements TwitterEventListener {

    private static final int TWEETS_LOADER_ID = 1;

    private ArrayAdapter<String> mAdapter;
    private TwitterServiceHelper mServiceHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // FIXME: 2.1.1

        // FIXME: 2.1.2

        // FIXME: 2.14

        // FIXME: 2.1.5

        // FIXME: 3.4
    }

    @Override
    protected void onResume() {
        super.onResume();

        // FIXME: 2.1.7

    }

    @Override
    protected void onPause() {
        super.onPause();

        // FIXME: 2.1.7
    }


    @Override
    public void onNewTweets(ArrayList<String> tweets) {
        // FIXME: 2.1.7

        // FIXME: 3.5

    }

    @Override
    public void onError() {
        // FIXME: 2.1.7

    }


    private void refreshTweets() {
        // FIXME: 2.1.6

    }


}