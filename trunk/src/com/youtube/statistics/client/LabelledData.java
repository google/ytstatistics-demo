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
