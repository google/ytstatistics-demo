// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.server;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Stores a recent query in the data store.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RecentQuery {
  @PrimaryKey
  @Persistent
  private String key;

  @Persistent
  private String jSessionId;

  @Persistent
  private String query;

  @Persistent
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
