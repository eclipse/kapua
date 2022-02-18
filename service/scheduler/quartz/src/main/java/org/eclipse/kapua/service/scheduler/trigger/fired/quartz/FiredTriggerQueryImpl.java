/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerQuery;

/**
 * {@link FiredTriggerQuery} implementation.
 *
 * @since 1.5.0
 */
public class FiredTriggerQueryImpl extends AbstractKapuaQuery implements FiredTriggerQuery {

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.5.0
     */
    public FiredTriggerQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
