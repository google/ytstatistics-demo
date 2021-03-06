// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
