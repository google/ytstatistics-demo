// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.ColumnChart;

import java.util.Date;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class YtStatistics implements EntryPoint {
  private static final String MSG_PUBLICATION_DATES = "Publication Dates";
  private static final String MSG_CLEAR = "Clear";
  private static final String MSG_CANCEL = "Cancel";
  private static final String MSG_QUERY = "Query";
  private static final String MSG_RECENT = "Recent";
  private static final String MSG_NO_RESULTS = "No results!";

  private static final String DEFAULT_SEARCH_QUERY = "MontyPython";

  private static final String SEARCH_FORM_ID = "searchForm";
  private static final String SEND_BUTTON_CONTAINER_ID = "sendButtonContainer";
  private static final String NAME_FIELD_CONTAINER_ID = "nameFieldContainer";
  private static final String RESULTS_ID = "results";

  private static final String CHART_TITLE_STYLE = "chartTitle";
  private static final String CLEAR_BUTTON_STYLE = "clearButton";
  private static final String SEND_BUTTON_STYLE = "sendButton";
  private static final String RECENT_BUTTON_STYLE = "recentButton";

  private Button sendButton;
  private Button clearButton;
  private Button recentButton;
  private TextBox nameField;
  private ResultsPanel resultsPanel;
  private RecentQueriesPanel recentQueries;

  /**
   * Create a remote service proxy to talk to the server-side YouTube statistics
   * service.
   */
  private final YouTubeStatisticsServiceAsync youTubeService =
      GWT.create(YouTubeStatisticsService.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    VisualizationUtils.loadVisualizationApi(new Runnable() {
      public void run() {
        loadUi();
      }
    }, ColumnChart.PACKAGE, AnnotatedTimeLine.PACKAGE);
  }

  public void loadUi() {
    sendButton = new Button(MSG_QUERY);
    clearButton = new Button(MSG_CLEAR);
    recentButton = new Button(MSG_RECENT);
    nameField = new TextBox();
    nameField.setText(DEFAULT_SEARCH_QUERY);

    // We can add style names to widgets.
    sendButton.addStyleName(SEND_BUTTON_STYLE);
    clearButton.addStyleName(CLEAR_BUTTON_STYLE);
    clearButton.setEnabled(false);
    recentButton.addStyleName(RECENT_BUTTON_STYLE);

    // Add the nameField and sendButton to the RootPanel.
    // Use RootPanel.get() to get the entire body element.
    RootPanel.get(NAME_FIELD_CONTAINER_ID).add(nameField);
    RootPanel.get(SEND_BUTTON_CONTAINER_ID).add(sendButton);
    RootPanel.get(SEND_BUTTON_CONTAINER_ID).add(clearButton);
    RootPanel.get(SEND_BUTTON_CONTAINER_ID).add(recentButton);

    // Focus the cursor on the name field when the app loads.
    nameField.setFocus(true);
    nameField.selectAll();

    // Use a RequestManager to handle canceling of requests.
    RequestManager.get().setRpcServer(youTubeService);

    // Create a panel for the results.
    resultsPanel = new ResultsPanel(RESULTS_ID);

    // Add a handler to clear the results panel.
    clearButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        RequestManager.get().cancelRequest();
        resultsPanel.clear();
        clearButton.setText(MSG_CANCEL);
        clearButton.setEnabled(false);
        sendButton.setEnabled(true);
        sendButton.setFocus(true);
      }
    });

    // Add handlers to send the name to the server
    sendButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        sendNameToServer();
      }
    });
    nameField.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          sendNameToServer();
        }
      }
    });

    // Add a handler to show the recent queries panel.
    recentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (recentQueries != null && recentQueries.isAttached() && recentQueries.isVisible()) {
          recentQueries.destroy();
          recentQueries = null;
        } else {
          recentQueries = new RecentQueriesPanel(recentButton);
          recentQueries.addTo(RootPanel.get(SEARCH_FORM_ID));
          recentQueries.fill(youTubeService, nameField, sendButton);
        }
      }
    });
  }

  /** Send the name to the server and wait for a response. */
  private void sendNameToServer() {
    sendButton.setEnabled(false);
    clearButton.setText(MSG_CANCEL);
    clearButton.setEnabled(true);
    resultsPanel.drawProgressIndicator();
    String textToServer = nameField.getText();
    RequestManager.get().makeRequest(textToServer, new AsyncCallback<StatisticsResponse>() {
      public void onFailure(Throwable caught) {
        clearButton.setText(MSG_CLEAR);
        sendButton.setEnabled(true);
        // Show the RPC error message to the user
        resultsPanel.drawRpcError();
        clearButton.setFocus(true);
      }

      public void onSuccess(StatisticsResponse result) {
        clearButton.setText(MSG_CLEAR);
        sendButton.setEnabled(true);
        clearButton.setFocus(true);
        if (result.isEmpty()) {
          resultsPanel.drawError(MSG_NO_RESULTS);
          return;
        }
        resultsPanel.clear();
        if (result.hasVideoDurations()) {
          drawDurationsGraph(resultsPanel, result.getVideoDurations());
        }
        if (result.hasPublicationDates()) {
          drawCommentsTimeline(resultsPanel, result.getPublicationDates());
        }
      }
    });
  }

  public void drawDurationsGraph(final ResultsPanel container,
      List<LabelledData<String, Integer>> durations) {
    VideoDurations columnGraph = new VideoDurations();
    container.addVisualization(columnGraph.drawChart(durations));
  }

  public void drawCommentsTimeline(final ResultsPanel container,
      List<LabelledData<Date, Integer>> times) {
    Label timelineTitle = new Label(MSG_PUBLICATION_DATES);
    timelineTitle.addStyleName(CHART_TITLE_STYLE);

    PublicationTimeline timeline = new PublicationTimeline();
    container.addVisualization(timelineTitle, timeline.drawChart(times));
  }
}
