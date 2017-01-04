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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableField;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

/**
 * Datastore object factory implementation
 * 
 * @since 1.0
 */
@KapuaProvider
public class DatastoreObjectFactoryImpl implements DatastoreObjectFactory
{

    @Override
    public ClientInfoQuery newClientInfoQuery()
    {
        return new ClientInfoQueryImpl();
    }

    @Override
    public ChannelInfoQuery newChannelInfoQuery()
    {
        return new ChannelInfoQueryImpl();
    }

    @Override
    public MetricInfoQuery newMetricInfoQuery()
    {
        return new MetricInfoQueryImpl();
    }

    @Override
    public <V> TermPredicate newTermPredicate(StorableField field, V value)
    {
        return new TermPredicateImpl(field, value);
    }
}
