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
