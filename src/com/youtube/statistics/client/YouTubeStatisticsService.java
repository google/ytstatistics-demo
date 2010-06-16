package com.youtube.statistics.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("query")
public interface YouTubeStatisticsService extends RemoteService {
  /** Queries YouTube for the given user, and returns statistics over that user's videos. */
  StatisticsResponse queryStatistics(String name);
  /** Looks up recent queried usernames in the data store. */
  String[] getRecentQueries();
}
