// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.Visualization;

import java.util.List;

/**
 * Renders a histogram of video durations.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class VideoDurations implements YouTubeChart<String, Integer> {
  private static final String MSG_VIDEOS = "Videos";
  private static final String MSG_DURATION = "Duration";
  private static final String MSG_VIDEO_DURATION = "Video duration";

  private static final int HEIGHT = 350;
  private static final int WIDTH = 400;

  @Override
  public Visualization<?> drawChart(List<LabelledData<String, Integer>> data) {
    ColumnChart chart = new ColumnChart(createLabelledTable(data), createDurationsOptions());
    chart.setWidth(WIDTH + "px");
    chart.getElement().getStyle().setProperty("display", "inline-block");
    return chart;
  }

  private ColumnChart.Options createDurationsOptions() {
    ColumnChart.Options options = ColumnChart.Options.create();
    options.setWidth(WIDTH);
    options.setHeight(HEIGHT);
    options.set3D(false);
    options.setTitle(MSG_VIDEO_DURATION);
    options.setLegend(LegendPosition.NONE);
    options.setTitleX(MSG_DURATION);
    options.setTitleY(MSG_VIDEOS);
    return options;
  }

  private AbstractDataTable createLabelledTable(List<LabelledData<String, Integer>> data) {
    DataTable table = DataTable.create();
    table.addColumn(ColumnType.STRING, MSG_DURATION);
    table.addColumn(ColumnType.NUMBER, MSG_VIDEOS);
    table.addRows(data.size());
    int row = 0;
    for (LabelledData<String, Integer> entry : data) {
      table.setValue(row, 0, entry.getLabel());
      table.setValue(row, 1, entry.getValue());
      ++row;
    }
    return table;
  }
}
