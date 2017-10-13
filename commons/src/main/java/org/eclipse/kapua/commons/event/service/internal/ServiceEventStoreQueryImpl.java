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
package org.eclipse.kapua.commons.event.service.internal;

import org.eclipse.kapua.commons.event.service.api.ServiceEvent;
import org.eclipse.kapua.commons.event.service.api.ServiceEventStoreQuery;
import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;

public class ServiceEventStoreQueryImpl extends AbstractKapuaQuery<ServiceEvent> implements ServiceEventStoreQuery {

    /**
     * Constructor
     */
    public ServiceEventStoreQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public ServiceEventStoreQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

}