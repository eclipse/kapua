/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.locator.KapuaLocator;
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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);

    /**
     * Creates a {@link MetricInfoListResult} instance
     *
     * @return
     */
    public MetricInfoListResult newMetricInfoListResult() {
        return DATASTORE_OBJECT_FACTORY.newMetricInfoListResult();
    }

    /**
     * Creates a {@link MetricInfoQuery} instance.
     *
     * @return
     */
    public MetricInfoQuery newQuery() {
        return DATASTORE_OBJECT_FACTORY.newMetricInfoQuery(null);
    }
}
