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

import java.util.Date;

/**
 * Stores a recent query in the data store.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 * 
 * @TODO Annotate this class for the datastore.
 */
public class RecentQuery {
  private String key;

  private String jSessionId;

  private String query;

  private Date date;

  public RecentQuery(String jSessionId, String query) {
    this.key = generateKey(jSessionId, query);
    this.jSessionId = jSessionId;
    this.query = query;
    this.date = new Date();
  }

  public String getKey() {
    return key;
  }

  public String getJSessionId() {
    return jSessionId;
  }

  public void setJSessionId(String jSessionId) {
    this.jSessionId = jSessionId;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public Date getDate() {
    return date;
  }

  public void updateDate() {
    this.date = new Date();
  }

  public static String generateKey(String jSessionId, String query) {
    return jSessionId + "^" + query;
  }
}
