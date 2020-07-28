/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.configuration;

public class ElasticsearchClientRequestConfiguration {

    private int requestRetryAttemptMax = 3;
    private int requestRetryAttemptWait = 2500;

    private int queryTimeout = 15000;
    private int scrollTimeout = 60000;

    public int getRequestRetryAttemptMax() {
        return requestRetryAttemptMax;
    }

    public void setRequestRetryAttemptMax(int requestRetryAttemptMax) {
        this.requestRetryAttemptMax = requestRetryAttemptMax;
    }

    public int getRequestRetryAttemptWait() {
        return requestRetryAttemptWait;
    }

    public void setRequestRetryAttemptWait(int requestRetryAttemptWait) {
        this.requestRetryAttemptWait = requestRetryAttemptWait;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public int getScrollTimeout() {
        return scrollTimeout;
    }

    public void setScrollTimeout(int scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
    }
}
