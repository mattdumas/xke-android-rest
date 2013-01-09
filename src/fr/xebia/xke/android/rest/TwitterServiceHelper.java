package fr.xebia.xke.android.rest;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.ArrayList;

public class TwitterServiceHelper {

    private static TwitterServiceHelper instance;

    // Subject for memory leaks
    private ArrayList<TwitterEventListener> mListeners = new ArrayList<TwitterEventListener>();

    private Context mContext;


    private TwitterServiceHelper(Context context) {
        mContext = context.getApplicationContext(); // avoid reference leak
    }


    public static TwitterServiceHelper getInstance(Context context) { // I know, not very secure ;)
        if (instance == null) {
            instance = new TwitterServiceHelper(context);
        }

        return instance;
    }


    public void addListener(TwitterEventListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(TwitterEventListener listener) {
        mListeners.remove(listener);
    }


    public void refreshTweets() {
        // FIXME: 2.2.1
        // Create intent to call TwitterService

        // Put extra data to intent : ResultReceiver

        // Start service with context member

    }


    private ResultReceiver getResultReceiver() {
        // FIXME: 2.2.1 - override onReceiveResult method

        return null;
    }


    public interface TwitterEventListener {

        void onNewTweets(ArrayList<String> tweets);

        void onError();

    }


}
