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

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.Metric;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

/**
 * Datastore object factory definition
 *
 * @since 1.0.0
 */
public interface DatastoreObjectFactory extends KapuaObjectFactory {

    /**
     * Return a new channel information query
     *
     * @param scopeId
     * @return
     * @since 1.0.0
     */
    ChannelInfoQuery newChannelInfoQuery(KapuaId scopeId);

    /**
     * Return a new channel information query
     *
     * @return
     * @since 1.0.0
     */
    ChannelInfoListResult newChannelInfoListResult();

    /**
     * Return a new client information query
     *
     * @param scopeId
     * @return
     * @since 1.0.0
     */
    ClientInfoQuery newClientInfoQuery(KapuaId scopeId);

    /**
     * Return a new client information query
     *
     * @return
     * @since 1.0.0
     */
    ClientInfoListResult newClientInfoListResult();

    /**
     * Return a new datastore message query
     *
     * @param scopeId
     * @return
     * @since 1.0.0
     */
    MessageQuery newDatastoreMessageQuery(KapuaId scopeId);

    /**
     * s
     * Return a new metric information query
     *
     * @return
     * @since 1.0.0
     */
    MessageListResult newDatastoreMessageListResult();

    /**
     * Return a new metric information query
     *
     * @param scopeId
     * @return
     * @since 1.0.0
     */
    MetricInfoQuery newMetricInfoQuery(KapuaId scopeId);

    /**
     * Return a new metric information query
     *
     * @return
     * @since 1.0.0
     */
    MetricInfoListResult newMetricInfoListResult();

    /**
     * Returns a new Metric instance
     *
     * @param name
     * @param value
     * @return
     */
    Metric<?> newMetric(String name, Object value);
}
