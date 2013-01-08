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
        // TODO : stop service here ?
    }

    public void refreshTweets() {
        // FIXME: create an intent to call TwitterService & pass it the ResultReceiver
        Intent intent = new Intent(mContext, TwitterService.class);

        intent.putExtra(TwitterService.EXTRA_RESULT_RECEIVER, getResultReceiver());

        // FIXME: start the service with the context member by passing the previous created intent
        mContext.startService(intent);
    }


    private ResultReceiver getResultReceiver() {
        // FIXME: return an anonymous inner class impl which notify registered listeners when it receive new tweets
        return new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == 200) {
                    ArrayList<String> tweets = resultData.getStringArrayList(TwitterService.TWEETS);
                    for (TwitterEventListener listener : mListeners) {
                        listener.onNewTweets(tweets);
                    }
                } else {
                    for (TwitterEventListener listener : mListeners) {
                        listener.onError();
                    }
                }
            }
        };
    }


    public interface TwitterEventListener {

        void onNewTweets(ArrayList<String> tweets);

        void onError();

    }


}
