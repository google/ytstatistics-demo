// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles an asynchronous request that the user can cancel. We don't support
 * parallel requests.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class RequestManager {
  // Singleton.
  private static final RequestManager INSTANCE = new RequestManager();

  private boolean isPendingRequestCancelled;

  private YouTubeStatisticsServiceAsync service;

  public static final RequestManager get() {
    return INSTANCE;
  }

  private RequestManager() {
  }

  public void setRpcServer(YouTubeStatisticsServiceAsync service) {
    this.service = service;
  }

  /**
   * Makes the request.
   */
  public void makeRequest(String textToSend, final AsyncCallback<StatisticsResponse> callback) {
    AsyncCallback<StatisticsResponse> callbackWrapper = new AsyncCallback<StatisticsResponse>() {
      @Override
      public void onFailure(Throwable caught) {
        if (!isPendingRequestCancelled) {
          callback.onFailure(caught);
        }
      }

      @Override
      public void onSuccess(StatisticsResponse result) {
        if (!isPendingRequestCancelled) {
          callback.onSuccess(result);
        }
      }
    };
    isPendingRequestCancelled = false;
    service.queryStatistics(textToSend, callbackWrapper);
  }

  /**
   * Cancels the given request.
   */
  public void cancelRequest() {
    isPendingRequestCancelled = true;
  }
}
