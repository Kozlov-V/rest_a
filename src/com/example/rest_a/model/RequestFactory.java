package com.example.rest_a.model;

import com.foxykeep.datadroid.requestmanager.Request;

public final class RequestFactory {
	public static final int REQUEST_TWEETS = 1;
	
	public static Request getTweetsRequest(String screenName) {
		Request request = new Request(REQUEST_TWEETS);
		request.put("screen_name", screenName);
		return request;
	}
	
	private RequestFactory() {
	}
}
