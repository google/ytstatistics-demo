// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.server;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Gets the date the videos from a YouTube video feed were published.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class PublicationDatesExtractor {
  private static final Logger log = Logger.getLogger(PublicationDatesExtractor.class.getName());

  private YouTubeService service;

  public PublicationDatesExtractor(YouTubeService service) {
    this.service = service;
  }

  /**
   * Extracts the dates the videos in a YouTube video feed were published.
   *
   * @param videoFeed the video feed whose publication dates to extract.
   * @return the dates as an array.
   */
  public Date[] extractPublicationDates(VideoFeed videoFeed) {
    Date start = new Date();
    List<VideoEntry> videoEntries = videoFeed.getEntries();
    List<DateTime> dateTimes = extractPublicationDates(videoEntries);
    List<Date> dates = new ArrayList<Date>();
    for (DateTime dateTime : dateTimes) {
      dates.add(new Date(dateTime.getValue()));
    }
    log.info("PublicationDatesExtractor>extractPublicationDates(VideoFeed), time: "
        + (new Date().getTime() - start.getTime()));
    return dates.toArray(new Date[0]);
  }

  private List<DateTime> extractPublicationDates(List<VideoEntry> videoEntries) {
    Date start = new Date();
    List<DateTime> dates = new ArrayList<DateTime>();
    for (VideoEntry entry : videoEntries) {
      dates.add(entry.getPublished());
    }
    log.info("PublicationDatesExtractor>extractPublicationDates(List), time: "
        + (new Date().getTime() - start.getTime()));
    return dates;
  }
}
