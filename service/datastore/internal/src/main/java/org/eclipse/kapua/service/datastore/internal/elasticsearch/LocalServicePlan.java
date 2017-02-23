/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.MetricsIndexBy;

public class LocalServicePlan {

    /**
     * Defines a value in service plan as unlimited resource
     */
    public static final int UNLIMITED = -1;

    /**
     * Defines a value in service plan as disabled resource
     */
    public static final int DISABLED = 0;

    private Date expirationDate = null;
    private boolean dataStorageEnabled = true;
    private int dataTimeToLive = 90;
    private DataIndexBy dataIndexBy = DataIndexBy.SERVER_TIMESTAMP;
    private MetricsIndexBy metricsIndexBy = MetricsIndexBy.TIMESTAMP;

    public LocalServicePlan(Map<String, Object> values) {
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean getDataStorageEnabled() {
        return dataStorageEnabled;
    }

    public void setDataStorageEnabled(boolean dataStorageEnabled) {
        this.dataStorageEnabled = dataStorageEnabled;
    }

    public int getDataTimeToLive() {
        return dataTimeToLive;
    }

    public void setDataTimeToLive(int dataTimeToLive) {
        this.dataTimeToLive = dataTimeToLive;
    }

    public DataIndexBy getDataIndexBy() {
        return dataIndexBy;
    }

    public void setDataIndexBy(DataIndexBy dataIndexBy) {
        this.dataIndexBy = dataIndexBy;
    }

    public MetricsIndexBy getMetricsIndexBy() {
        return metricsIndexBy;
    }

    public void setMetricsIndexBy(MetricsIndexBy metricsIndexBy) {
        this.metricsIndexBy = metricsIndexBy;
    }
}
