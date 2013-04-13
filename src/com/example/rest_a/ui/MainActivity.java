package com.example.rest_a.ui;

import com.example.rest_a.R;
import com.example.rest_a.model.RequestFactory;
import com.example.rest_a.model.RestRequestManager;
import com.example.rest_a.model.provider.Contract.Tweets;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.database.Cursor;

public class MainActivity extends FragmentActivity {
	
	final String TAG = getClass().getSimpleName();

	private PullToRefreshListView listView;
	private SimpleCursorAdapter adapter;
	
	private RestRequestManager requestManager;
	
	private static final int LOADER_ID = 1;
	private static final String[] PROJECTION = { 
		Tweets._ID,
		Tweets.USER_NAME,
		Tweets.BODY
	};
	
	private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int loaderId, Bundle arg1) {
			return new CursorLoader(
				MainActivity.this,
				Tweets.CONTENT_URI,
				PROJECTION,
				null,
				null,
				null
			);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
			adapter.swapCursor(cursor);
			if (cursor.getCount() == 0) {
				update();
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			adapter.swapCursor(null);
		}
	};
	
	RequestListener requestListener = new RequestListener() {
		
		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			listView.onRefreshComplete();
		}
		
		void showError() {
			listView.onRefreshComplete();
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.
				setTitle(android.R.string.dialog_alert_title).
				setMessage(getString(R.string.faled_to_load_data)).
				create().
				show();
		}
		
		@Override
		public void onRequestDataError(Request request) {
			showError();
		}
		
		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			showError();
		}
		
		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			showError();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (PullToRefreshListView)findViewById(R.id.listView);
		adapter = new SimpleCursorAdapter(this,
			R.layout.tweet_view, 
			null, 
			new String[]{ Tweets.USER_NAME, Tweets.BODY },
			new int[]{ R.id.user_name_text_view, R.id.body_text_view }, 
			0);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				update();
			}
		});
		
		getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
		
		requestManager = RestRequestManager.from(this);
	}
	
	void update() {
		listView.setRefreshing();
		Request updateRequest = new Request(RequestFactory.REQUEST_TWEETS);
		updateRequest.put("screen_name", "habrahabr");
		requestManager.execute(updateRequest, requestListener);
	}

}
