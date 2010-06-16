// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The data returned by the {@link YouTubeStatisticsService}.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class StatisticsResponse implements Serializable {
  private List<LabelledData<String, Integer>> videoDurations;

  public boolean hasVideoDurations() {
    return videoDurations != null && !videoDurations.isEmpty();
  }

  public List<LabelledData<String, Integer>> getVideoDurations() {
    return videoDurations;
  }

  public void setVideoDurations(List<LabelledData<String, Integer>> videoDurations) {
    this.videoDurations = videoDurations;
  }

  private List<LabelledData<Date, Integer>> publicationDates;

  public boolean hasPublicationDates() {
    return publicationDates != null && !publicationDates.isEmpty();
  }

  public List<LabelledData<Date, Integer>> getPublicationDates() {
    return publicationDates;
  }

  public void setPublicationDates(List<LabelledData<Date, Integer>> commentDates) {
    this.publicationDates = commentDates;
  }

  public boolean isEmpty() {
    return videoDurations.isEmpty() && publicationDates.isEmpty();
  }
}
