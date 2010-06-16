// Copyright 2010 Google Inc. All Rights Reserved.

package com.youtube.statistics.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Singleton {@link PersistenceManagerFactory}.
 *
 * @author martinstrauss@google.com (Martin Strauss)
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}