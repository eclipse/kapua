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
package org.eclipse.kapua.service.datastore.internal.model.query;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.AbstractStorableQuery;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;

/**
 * Metric information query implementation
 * 
 * @since 1.0.0
 */
public class MetricInfoQueryImpl extends AbstractStorableQuery<MetricInfo> implements MetricInfoQuery {

    /**
     * Constructor.
     * 
     * @param scopeId
     * 
     * @since 1.0.0
     */
    public MetricInfoQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String[] getIncludes(StorableFetchStyle fetchStyle) {
        return new String[] { "*" };
    }

    @Override
    public String[] getExcludes(StorableFetchStyle fetchStyle) {
        return new String[] { "" };
    }

    @Override
    public String[] getFields() {
        return new String[] { MetricInfoField.SCOPE_ID.field(),
                MetricInfoField.CLIENT_ID.field(),
                MetricInfoField.CHANNEL.field(),
                MetricInfoField.NAME_FULL.field(),
                MetricInfoField.TYPE_FULL.field(),
                MetricInfoField.TIMESTAMP_FULL.field(),
                MetricInfoField.MESSAGE_ID_FULL.field() };
    }

}
