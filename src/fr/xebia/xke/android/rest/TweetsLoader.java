package fr.xebia.xke.android.rest;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import static fr.xebia.xke.android.rest.DatabaseHelper.Tables;
import static fr.xebia.xke.android.rest.DatabaseHelper.TweetColumns;

public class TweetsLoader extends AsyncTaskLoader<Cursor> {

    private static final String TAG = TweetsLoader.class.getName();

    private SQLiteOpenHelper mSqLiteOpenHelper;

    private Cursor mCursor;


    public TweetsLoader(Context context) {
        super(context);
        mSqLiteOpenHelper = new DatabaseHelper(context);
    }


    @Override
    public Cursor loadInBackground() {
        Log.i(TAG, "Loading tweets from SQLite DB");

        // FIXME: 3.2
        // Use SQLiteQueryBuilder
        // Set tables Table.Tweets
        // Query DB and retrieve cursor

        return null;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped.
            closeQuietly(cursor);
            return;
        }

        Cursor previousCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (previousCursor != cursor) {
            closeQuietly(previousCursor);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor == null) {
            forceLoad();
        } else {
            deliverResult(mCursor);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        super.onCanceled(cursor);

        closeQuietly(cursor);
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mCursor != null) {
            closeQuietly(mCursor);
            mCursor = null;
        }
    }


    public static void closeQuietly(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) return;

        try {
            cursor.close();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unable to properly close cursor.", e);
        }
    }

}
