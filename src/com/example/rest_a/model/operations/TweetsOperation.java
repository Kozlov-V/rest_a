package com.example.rest_a.model.operations;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import com.example.rest_a.model.provider.Contract;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.network.NetworkConnection;
import com.foxykeep.datadroid.network.NetworkConnection.ConnectionResult;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService.Operation;

public final class TweetsOperation implements Operation {
	@Override
	public Bundle execute(Context context, Request request)
			throws ConnectionException, DataException, CustomRequestException {
		NetworkConnection connection = new NetworkConnection(context, "https://api.twitter.com/1/statuses/user_timeline.json");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("screen_name", request.getString("screen_name"));
		connection.setParameters(params);
		ConnectionResult result = connection.execute();
		ContentValues[] tweetsValues;
		try {
			JSONArray tweetsJson = new JSONArray(result.body);
			tweetsValues = new ContentValues[tweetsJson.length()];
			
			for (int i = 0; i < tweetsJson.length(); ++i) {
				ContentValues tweet = new ContentValues();
				tweet.put("user_name", tweetsJson.getJSONObject(i).getJSONObject("user").getString("name"));
				tweet.put("body", tweetsJson.getJSONObject(i).getString("text"));
				tweetsValues[i] = tweet;
			}
		} catch (JSONException e) {
			throw new DataException(e.getMessage());
		}
		
		context.getContentResolver().delete(Contract.Tweets.CONTENT_URI, null, null);
		context.getContentResolver().bulkInsert(Contract.Tweets.CONTENT_URI, tweetsValues);
		return null;
	}
	
}
