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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteriaImpl;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceQuery;

/**
 * {@link DeviceQuery} implementation.
 *
 * @since 1.0.0
 */
public class DeviceQueryImpl extends AbstractKapuaQuery implements DeviceQuery {

    /**
     * Constructor
     */
    private DeviceQueryImpl() {
        super();

        setSortCriteria(new FieldSortCriteriaImpl(DeviceAttributes.CLIENT_ID, SortOrder.ASCENDING));
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

    @Override
    public <T> DeviceMatchPredicateImpl<T> matchPredicate(T matchTerm) {
        return new DeviceMatchPredicateImpl<>(matchTerm);
    }

}
