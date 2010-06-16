// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.client;

import java.io.Serializable;

/**
 * Represents a labeled data point.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 *
 * @param <L> the type of the label
 * @param <D> the type of the data
 */
public class LabelledData<L, D> implements Serializable {

  public LabelledData() {
  }

  public LabelledData(L label, D value) {
    this.label = label;
    this.value = value;
  }

  public L getLabel() {
    return label;
  }

  public void setLabel(L label) {
    this.label = label;
  }

  public D getValue() {
    return value;
  }

  public void setValue(D value) {
    this.value = value;
  }

  private L label;
  private D value;
}
