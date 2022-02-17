/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery;

/**
 * {@link TriggerDefinitionQuery} implementation.
 *
 * @since 1.1.0
 */
public class TriggerDefinitionQueryImpl extends AbstractKapuaNamedQuery implements TriggerDefinitionQuery {

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.1.0
     */
    public TriggerDefinitionQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
