/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteriaImpl;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.event.DeviceEventAttributes;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;

/**
 * Device event query.
 *
 * @since 1.0
 */
public class DeviceEventQueryImpl extends AbstractKapuaQuery implements DeviceEventQuery {

    /**
     * Constructor
     */
    private DeviceEventQueryImpl() {
        super();

        setSortCriteria(new FieldSortCriteriaImpl(DeviceEventAttributes.RECEIVED_ON, SortOrder.DESCENDING));
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceEventQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
