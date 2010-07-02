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

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.Visualization;

import java.util.Date;
import java.util.List;

/**
 * Renders a list of video counts labelled by publication date on a timeline.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class PublicationTimeline implements YouTubeChart<Date, Integer> {
  private static final String MSG_DATE = "Date";
  private static final String MSG_VIDEOS = "Videos";

  private static final int HEIGHT = 300;
  private static final int WIDTH = 400;

  @Override
  public Visualization<?> drawChart(List<LabelledData<Date, Integer>> data) {
    String width = WIDTH + "px";
    String height = HEIGHT + "px";
    AnnotatedTimeLine timeline =
        new AnnotatedTimeLine(createDateTable(data), AnnotatedTimeLine.Options.create(), width,
            height);
    return timeline;
  }

  private AbstractDataTable createDateTable(List<LabelledData<Date, Integer>> data) {
    DataTable table = DataTable.create();
    table.addColumn(ColumnType.DATETIME, MSG_DATE);
    table.addColumn(ColumnType.NUMBER, MSG_VIDEOS);
    table.addRows(data.size());
    int row = 0;
    for (LabelledData<Date, Integer> entry : data) {
      table.setValue(row, 0, entry.getLabel());
      table.setValue(row, 1, entry.getValue());
      ++row;
    }
    return table;
  }
}
