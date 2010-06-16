// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import com.google.gwt.visualization.client.visualizations.Visualization;

import java.util.List;

/**
 * Classes implementing this interface generate a {@link Visualization} that can
 * be shown in the UI.
 *
 * @param <L> The type of the data labels.
 * @param <V> The type of the data values.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public interface YouTubeChart<L, V> {
  /**
   * Generates a {@link Visualization} for the given data.
   */
  Visualization<?> drawChart(List<LabelledData<L, V>> data);
}
