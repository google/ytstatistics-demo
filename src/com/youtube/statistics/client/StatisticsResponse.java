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
import java.util.Date;
import java.util.List;

/**
 * The data returned by the {@link YouTubeStatisticsService}.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class StatisticsResponse implements Serializable {
  
  // TODO: add code to store video durations.
  
  private List<LabelledData<Date, Integer>> publicationDates;

  public boolean hasPublicationDates() {
    return publicationDates != null && !publicationDates.isEmpty();
  }

  public List<LabelledData<Date, Integer>> getPublicationDates() {
    return publicationDates;
  }

  public void setPublicationDates(List<LabelledData<Date, Integer>> commentDates) {
    this.publicationDates = commentDates;
  }

  public boolean isEmpty() {
    // TODO: take video durations into consideration.
    return publicationDates.isEmpty();
  }
}
