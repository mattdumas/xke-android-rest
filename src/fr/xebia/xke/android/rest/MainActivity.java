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

        // FIXME: 2.1.1 - Init ArrayAdapter with the 2 args constructor

        // FIXME: 2.1.2 - set the adataper to ListActivity

        // FIXME: 2.1.3 (optional) - Put hard coded data into adapter & launch the app

        // FIXME: 2.1.4

        // FIXME: 2.1.5 - Use findViewById

        // FIXME: 3.4 - Use initLoader method of LoaderManager
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
        // FIXME: 2.1.6 - Toast notification (Toast.makeText) and delegate to TwitterServiceHelper

    }


}