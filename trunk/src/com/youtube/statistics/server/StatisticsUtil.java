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

package com.youtube.statistics.server;

import com.youtube.statistics.client.LabelledData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Some useful utility functions for statistics.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public class StatisticsUtil {
  /**
   * Formats a value range (as pair of values) into a label that can be used in
   * a histogram.
   */
  public static interface LabelFormatter {
    String formatRange(int lowerBound, int upperBound);
  }

  /**
   * Formats a pair of integers representing duration in seconds.
   */
  public static class DurationFormatter implements LabelFormatter {
    @Override
    public String formatRange(int lowerBound, int upperBound) {
      String units = "secs";
      String range = lowerBound + "-" + upperBound;
      if (upperBound - lowerBound > 60) {
        units = "mins";
        range = (lowerBound / 60) + "-" + (upperBound / 60);
      }
      return range + " " + units;
    }
  }

  /**
   * Buckets an array of integers into the given number of buckets, for display
   * on a histogram.
   */
  public static List<LabelledData<String, Integer>> bucketData(int numBuckets, int[] data,
      LabelFormatter formatter) {
    if (data.length <= 0) {
      return new ArrayList<LabelledData<String, Integer>>();
    }

    Arrays.sort(data);

    // Sort the data into buckets.
    int bucketSize = (data[data.length - 1] - data[0]) / numBuckets;

    int[] bucketUpperBounds = new int[numBuckets];
    bucketUpperBounds[0] = data[0] + bucketSize;
    for (int i = 1; i < numBuckets; ++i) {
      bucketUpperBounds[i] = bucketUpperBounds[i - 1] + bucketSize;
    }

    int[] bucketContents = new int[numBuckets];
    for (int duration : data) {
      for (int i = 0; i < numBuckets; ++i) {
        if (duration < bucketUpperBounds[i]) {
          ++bucketContents[i];
          break;
        }
      }
    }

    // We can't use a Map because we want the entries in a particular order.
    List<LabelledData<String, Integer>> labelledData =
        new ArrayList<LabelledData<String, Integer>>();
    for (int i = 0; i < numBuckets; ++i) {
      String label = formatter.formatRange(bucketUpperBounds[i] - bucketSize, bucketUpperBounds[i]);
      labelledData.add(new LabelledData<String, Integer>(label, bucketContents[i]));
    }

    return labelledData;
  }

  public static List<LabelledData<Date, Integer>> bucketTimeData(int minimumNumBuckets, Date[] data) {
    if (data.length <= 0) {
      return new ArrayList<LabelledData<Date, Integer>>();
    }

    Arrays.sort(data);

    // Intelligently select a bucket size.
    long bucketSizeInMilliseconds;

    long millisecondsInOneDay = 24 * 60 * 60 * 1000;
    long millisecondsInOneWeek = 7 * millisecondsInOneDay;
    long range = data[data.length - 1].getTime() - data[0].getTime();
    if (range > minimumNumBuckets * millisecondsInOneWeek) {
      // bucket into weeks.
      bucketSizeInMilliseconds = millisecondsInOneWeek;
    } else  {
      // bucket into days.
      bucketSizeInMilliseconds = millisecondsInOneDay;
    }

    // Sort the data into buckets.
    int numBuckets = (int) (range / bucketSizeInMilliseconds);

    Date[] bucketUpperBounds = new Date[numBuckets];
    bucketUpperBounds[0] = new Date(data[0].getTime() + bucketSizeInMilliseconds);
    for (int i = 1; i < numBuckets; ++i) {
      bucketUpperBounds[i] =
          new Date(bucketUpperBounds[i - 1].getTime() + bucketSizeInMilliseconds);
    }

    int[] bucketContents = new int[numBuckets];
    for (Date commentDate : data) {
      for (int i = 0; i < numBuckets; ++i) {
        if (commentDate.before(bucketUpperBounds[i])) {
          ++bucketContents[i];
          break;
        }
      }
    }

    // We can't use a Map because we want the entries in a particular order.
    List<LabelledData<Date, Integer>> labelledData = new ArrayList<LabelledData<Date, Integer>>();
    for (int i = 0; i < numBuckets; ++i) {
      labelledData.add(new LabelledData<Date, Integer>(bucketUpperBounds[i], bucketContents[i]));
    }

    return labelledData;
  }
}
