package com.example.rest_a.model.provider;

import com.example.rest_a.model.provider.Contract.Tweets;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class RestProvider extends ContentProvider {
	final String TAG = getClass().getSimpleName();
	
	private static final String TABLE_TWEETS = "tweets";
	
	private static final String DB_NAME = TABLE_TWEETS + ".db";
	private static final int DB_VERSION = 1;
	
	private static final UriMatcher sUriMatcher;
	
	private static final int PATH_ROOT = 0;
	private static final int PATH_TWEETS = 1;
	
	static {
		sUriMatcher = new UriMatcher(PATH_ROOT);
		sUriMatcher.addURI(Contract.AUTHORITY, Contract.Tweets.CONTENT_PATH, PATH_TWEETS); 
	}
	
	private DatabaseHeloper mDatabaseHelper;
	
	class DatabaseHeloper extends SQLiteOpenHelper {

		public DatabaseHeloper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = 
				"create table " + TABLE_TWEETS + " (" + 
					Tweets._ID + " integer primary key autoincrement, " +
					Tweets.USER_NAME + " text, " +
					Tweets.BODY + " text, " +
					Tweets.DATE + " text" +
				")";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHeloper(getContext(), DB_NAME, null, DB_VERSION);
		return true;
	}
	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS: {
			Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_TWEETS, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), Contract.Tweets.CONTENT_URI);
			return cursor;
		}
		default:
			return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS:
			return Contract.Tweets.CONTENT_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS: {
			mDatabaseHelper.getWritableDatabase().insert(TABLE_TWEETS, null, values);
			getContext().getContentResolver().notifyChange(Contract.Tweets.CONTENT_URI, null);
		}
		default:
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS:
			return mDatabaseHelper.getWritableDatabase().delete(TABLE_TWEETS, selection, selectionArgs);
		default:
			return 0;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS:
			return mDatabaseHelper.getWritableDatabase().update(TABLE_TWEETS, values, selection, selectionArgs);
		default:
			return 0;
		}
	}

}
