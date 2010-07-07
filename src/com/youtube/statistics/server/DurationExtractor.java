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
    for (YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
      duration += mediaContent.getDuration();
    }
    log.info("DurationExtractor>extractDuration(), time: "
        + (new Date().getTime() - start.getTime()));
    return duration;
  }
}
