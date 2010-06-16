// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.visualizations.Visualization;

/**
 * A panel to show the results of the RPC requst, or the progress indicator or
 * error message as appropriate.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class ResultsPanel {
  /** The title for the error message when the server returns an error. */
  private static final String MSG_RPC_FAILURE = "Remote Procedure Call - Failure";
  /** The prefix for all error messages. */
  private static final String MSG_ERROR = "Error";
  /** Displayed when the server cannot be reached or returns an error. */
  private static final String MSG_SERVER_ERROR =
      "An error occurred while " + "attempting to contact the server. Please check your network "
          + "connection and try again.";

  private static final String PROGRESS_SPINNER_STYLE = "progressSpinner";

  private static final String SERVER_RESPONSE_LABEL_ERROR_ID = "serverResponseLabelError";

  private static final String PROGRESS_SPINNER_IMAGE = "spin.gif";

  private final RootPanel panel;

  public ResultsPanel(String domId) {
    panel = RootPanel.get(domId);
  }

  public void drawProgressIndicator() {
    panel.clear();
    Image progress = new Image(PROGRESS_SPINNER_IMAGE);
    progress.addStyleName(PROGRESS_SPINNER_STYLE);
    panel.add(progress);
  }

  public void drawError(String error) {
    panel.clear();
    panel.add(new Label(MSG_ERROR));
    final Label serverResponseLabel = new Label();
    panel.add(serverResponseLabel);
    serverResponseLabel.addStyleName(SERVER_RESPONSE_LABEL_ERROR_ID);
    serverResponseLabel.setText(error);
  }

  public void drawRpcError() {
    panel.clear();
    panel.add(new Label(MSG_RPC_FAILURE));
    final HTML serverResponseLabel = new HTML();
    panel.add(serverResponseLabel);
    serverResponseLabel.addStyleName(SERVER_RESPONSE_LABEL_ERROR_ID);
    serverResponseLabel.setHTML(MSG_SERVER_ERROR);
  }

  public void addVisualization(Visualization<?> chart) {
    panel.add(chart);
  }

  public void addVisualization(Label title, Visualization<?> chart) {
    Panel visualizationPanel = new FlowPanel();
    visualizationPanel.addStyleName("visualizationPanel");
    visualizationPanel.add(title);
    visualizationPanel.add(chart);
    panel.add(visualizationPanel);
  }

  public void clear() {
    panel.clear();
  }
}
