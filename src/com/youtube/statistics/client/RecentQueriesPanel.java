// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Shows recent queries to the server.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class RecentQueriesPanel {
  private static final String MSG_NO_RECENT_QUERIES = "No recent queries!";

  private static final String PROGRESS_SPINNER_STYLE = "progressSpinner";
  private static final String PROGRESS_SPINNER_IMAGE = "spin.gif";

  private final Panel panel;

  public RecentQueriesPanel(Widget parent) {
    panel = new VerticalPanel();
    panel.addStyleName("recentQueriesPanel");

    // Calculate the position to be twice the width of the button, and align
    // with the right hand edge of the parent, allowing for a margin.
    int margin = 2;
    int width = 2 * parent.getOffsetWidth();
    int top = parent.getAbsoluteTop() + parent.getOffsetHeight() + margin;
    int right = parent.getAbsoluteLeft() + parent.getOffsetWidth() - margin;
    int left = right - width;

    panel.getElement().getStyle().setPropertyPx("left", left);
    panel.getElement().getStyle().setPropertyPx("top", top);
    panel.getElement().getStyle().setPropertyPx("width", width);
    panel.setVisible(false);
  }

  public void fill(YouTubeStatisticsServiceAsync service, final TextBox queryField,
      final Button queryButton) {
    drawProgressIndicator();
    panel.setVisible(true);
    service.getRecentQueries(new AsyncCallback<String[]>() {

      @Override
      public void onSuccess(String[] result) {
        if (result.length <= 0) {
          onFailure(null);
          return;
        }
        panel.clear();
        for (final String query : result) {
          Anchor anchor = new Anchor(query);
          anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              queryField.setText(query);
              queryButton.click();
              destroy();
            }
          });
          panel.add(anchor);
        }
      }

      @Override
      public void onFailure(Throwable caught) {
        panel.clear();
        Label label = new Label(MSG_NO_RECENT_QUERIES);
        panel.add(label);
      }
    });
  }

  private void drawProgressIndicator() {
    panel.clear();
    Image progress = new Image(PROGRESS_SPINNER_IMAGE);
    progress.addStyleName(PROGRESS_SPINNER_STYLE);
    panel.add(progress);
  }

  /**
   * Determines whether the underlying widget is currently attached to the
   * browser's document (i.e., there is an unbroken chain of widgets between
   * this widget and the underlying browser document).
   *
   * @return true if the widget is attached
   */
  public boolean isAttached() {
    return panel.isAttached();
  }

  /**
   * Determines whether or not the underlying object is visible.
   *
   * @return true if the object is visible
   */
  public boolean isVisible() {
    return panel.isVisible();
  }

  /**
   * Adds the widget to a parent.
   */
  public void addTo(Panel parent) {
    parent.add(panel);
  }

  /**
   * Hides the widget and removes it from its parent.
   */
  public void destroy() {
    panel.clear();
    panel.setVisible(false);
    panel.removeFromParent();
  }
}
