package fr.xebia.xke.android.rest;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tweets";

    public static class Tables {
        public static final String TWEETS = "tweet";
    }

    public static interface TweetColumns {
        public static final String TWEET_CONTENT = "content";
    }

    private static final String TABLE_CREATE = "CREATE TABLE " + Tables.TWEETS + " (" + TweetColumns.TWEET_CONTENT + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no op
    }

}
