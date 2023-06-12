/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.model.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.MetricInfoFactory;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Metric information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class MetricInfoXmlRegistry {

    private final MetricInfoFactory metricInfoFactory = KapuaLocator.getInstance().getFactory(MetricInfoFactory.class);

    /**
     * Creates a {@link MetricInfoListResult} instance
     *
     * @return
     */
    public MetricInfoListResult newListResult() {
        return metricInfoFactory.newListResult();
    }

    /**
     * Creates a {@link MetricInfoQuery} instance.
     *
     * @return
     */
    public MetricInfoQuery newQuery() {
        return metricInfoFactory.newQuery(null);
    }
}
