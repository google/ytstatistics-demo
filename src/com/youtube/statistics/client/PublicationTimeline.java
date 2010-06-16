// Copyright 2010 Google Inc. All Rights Reserved.

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
