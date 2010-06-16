// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.server;

import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Extracts durations from a YouTube video feed.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class DurationExtractor {
  private static final Logger log = Logger.getLogger(PublicationDatesExtractor.class.getName());

  /**
   * Extracts durations from a YouTube video feed.
   *
   * @param videoFeed the video feed whose durations to extract.
   * @return the durations of the video feed as an array of int.
   */
  public int[] extractDurations(VideoFeed videoFeed) {
    Date start = new Date();
    List<VideoEntry> videoEntries = videoFeed.getEntries();
    int[] result = new int[videoEntries.size()];
    for (int i = 0; i < videoEntries.size(); ++i) {
      result[i] = extractDuration(videoEntries.get(i));
    }
    log.info("DurationExtractor>extractDurations(), time: "
        + (new Date().getTime() - start.getTime()));
    return result;
  }

  private static int extractDuration(VideoEntry videoEntry) {
    Date start = new Date();
    int duration = 0;
    YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
    // TODO(martinstrauss): is this logic logical?
    for (YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
      duration += mediaContent.getDuration();
    }
    log.info("DurationExtractor>extractDuration(), time: "
        + (new Date().getTime() - start.getTime()));
    return duration;
  }
}
