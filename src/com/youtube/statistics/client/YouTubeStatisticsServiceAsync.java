package com.youtube.statistics.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>YouTubeStatisticsService</code>.
 */
public interface YouTubeStatisticsServiceAsync {
  void queryStatistics(String input, AsyncCallback<StatisticsResponse> callback);
  void getRecentQueries(AsyncCallback<String[]> callback);
}
