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

package com.youtube.statistics.server;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.youtube.statistics.client.StatisticsResponse;
import com.youtube.statistics.client.YouTubeStatisticsService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * The server side implementation of the RPC service.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
@SuppressWarnings("serial")
public class YouTubeStatisticsServiceImpl extends RemoteServiceServlet implements
    YouTubeStatisticsService {
  // According to the YouTube product dashboard, you no longer need to provide a
  // Client ID with YouTube API requests.
  private static final String clientID = "";
  private static final String developer_key =
      "AI39si4W-_TB8yn3woJgXYM8hIuMhXd-KWiRwUWSmDhUIBWhTdQS6sHpNexzfzwf"
          + "VHM6wWZVPoVW6rMx4vLak8StvzNHpuSOeA";

  private static final Logger log = Logger.getLogger(PublicationDatesExtractor.class.getName());

  public StatisticsResponse queryStatistics(String input) {
    Date start = new Date();
    storeRecentQuery(input);
    StatisticsResponse response = new StatisticsResponse();
    YouTubeService service = new YouTubeService(clientID, developer_key);

    String feedUrl = "http://gdata.youtube.com/feeds/api/users/" + input + "/uploads";
    VideoFeed videoFeed;
    try {
      videoFeed = service.getFeed(new URL(feedUrl), VideoFeed.class);
      if (videoFeed.getTotalResults() == 0) {
        return response;
      }

      DurationExtractor durationExtractor = new DurationExtractor();
      int[] durations = durationExtractor.extractDurations(videoFeed);
      response.setVideoDurations(StatisticsUtil.bucketData(8, durations,
          new StatisticsUtil.DurationFormatter()));

      PublicationDatesExtractor publicationDatesExtractor = new PublicationDatesExtractor();
      Date[] dates = publicationDatesExtractor.extractPublicationDates(videoFeed);
      response.setPublicationDates(StatisticsUtil.bucketTimeData(20, dates));
    } catch (MalformedURLException e) {
      log(e.toString(), e);
    } catch (IOException e) {
      log(e.toString(), e);
    } catch (ServiceException e) {
      log(e.toString(), e);
    }
    log.info("YouTubeStatisticsServiceImpl>queryStatistics(), time: "
        + (new Date().getTime() - start.getTime()));
    return response;
  }

  public String[] getRecentQueries() {
    Date start = new Date();
    String jSessionId = this.getThreadLocalRequest().getSession().getId();
    List<RecentQuery> results = lookupAllQueries(jSessionId);
    String[] response = new String[results.size()];
    for (int i = 0; i < results.size(); ++i) {
      response[i] = results.get(i).getQuery();
    }
    log.info("YouTubeStatisticsServiceImpl>getRecentQueries(), time: "
        + (new Date().getTime() - start.getTime()));
    return response;
  }

  @SuppressWarnings("unchecked")
  // JDO needs an unchecked cast.
  private List<RecentQuery> lookupAllQueries(String jSessionId) {
    Date start = new Date();
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(RecentQuery.class);
    query.setFilter("jSessionId == sessionIdParam");
    query.setOrdering("date desc");
    query.declareParameters("String sessionIdParam");
    log.info("YouTubeStatisticsServiceImpl>lookupAllQueries(), time: "
        + (new Date().getTime() - start.getTime()));
    try {
      return (List<RecentQuery>) query.execute(jSessionId);
    } finally {
      query.closeAll();
    }
  }

  private void storeRecentQuery(String query) {
    Date start = new Date();
    String jSessionId = this.getThreadLocalRequest().getSession().getId();
    PersistenceManager pm = PMF.get().getPersistenceManager();
    RecentQuery recent;
    try {
      recent = lookupSingleQuery(pm, jSessionId, query);
      if (recent != null) {
        recent.updateDate();
      } else {
        recent = new RecentQuery(jSessionId, query);
        pm.makePersistent(recent);
      }
    } catch (JDOObjectNotFoundException e) {
      recent = new RecentQuery(jSessionId, query);
      pm.makePersistent(recent);
    } finally {
      pm.close();
    }
    log.info("YouTubeStatisticsServiceImpl>storeRecentQuery(), time: "
        + (new Date().getTime() - start.getTime()));
  }

  private RecentQuery lookupSingleQuery(PersistenceManager pm, String jSessionId, String query) {
    String key = RecentQuery.generateKey(jSessionId, query);
    return pm.getObjectById(RecentQuery.class, key);
  }
}
