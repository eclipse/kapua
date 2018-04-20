/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.metric;

/**
 * {@link MetricsService} factory.
 * 
 * @since 1.0
 */
public class MetricServiceFactory {

    private static MetricsService instance;

    private MetricServiceFactory() {

    }

    /**
     * Get the {@link MetricsService} singleton instance
     * 
     * @return
     */
    public static MetricsService getInstance() {
        if (instance == null) {
            synchronized (MetricServiceFactory.class) {
                if (instance == null) {
                    instance = new MetricsServiceImpl();
                }
            }
        }
        return instance;
    }

}
