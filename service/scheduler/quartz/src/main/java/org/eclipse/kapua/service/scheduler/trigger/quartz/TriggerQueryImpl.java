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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;

/**
 * User roles factory service implementation.
 *
 * @since 1.0
 */
public class TriggerQueryImpl extends AbstractKapuaQuery<Trigger> implements TriggerQuery {

    /**
     * Constructor
     */
    private TriggerQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public TriggerQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

}
