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
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

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

    /**
     * Create and keep a copy of the given query
     * 
     * @param query
     * 
     * @since 1.0.0
     */
    public void copy(MetricInfoQuery query) {
        super.copy(query);
        // Add copy for local members
    }
}
