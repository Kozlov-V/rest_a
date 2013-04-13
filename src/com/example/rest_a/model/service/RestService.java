package com.example.rest_a.model.service;

import com.example.rest_a.model.RequestFactory;
import com.example.rest_a.model.operations.TweetsOperation;
import com.foxykeep.datadroid.service.RequestService;

public class RestService extends RequestService {

	@Override
	public Operation getOperationForType(int requestType) {
		switch (requestType) {
		case RequestFactory.REQUEST_TWEETS:
			return new TweetsOperation();
		default:
			return null;
		}
	}
	
}
