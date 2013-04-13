package com.example.rest_a.model.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {
	public static final String AUTHORITY = "com.example.rest_a";
	
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	
	public interface TweetsCoulmns {
		public static final String USER_NAME = "user_name";
		public static final String BODY = "body";
		public static final String DATE = "date";
	}
	
	public static final class Tweets implements BaseColumns, TweetsCoulmns {
		public static final String CONTENT_PATH = "tweets";
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
	}
}
