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
import java.util.logging.Logger;

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

      // TODO: implement video durations.

      PublicationDatesExtractor publicationDatesExtractor = new PublicationDatesExtractor(service);
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
    // TODO: implement looking up recent queries.
    return new String[0];
  }

  private void storeRecentQuery(String query) {
    Date start = new Date();
    String jSessionId = this.getThreadLocalRequest().getSession().getId();
    // TODO: implement storing recent queries.
  }
}
