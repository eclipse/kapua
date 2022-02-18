/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
