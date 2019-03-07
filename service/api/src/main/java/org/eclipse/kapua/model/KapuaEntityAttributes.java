/*******************************************************************************
 * Copyright (c) 2016, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model;

import org.eclipse.kapua.model.query.KapuaQuery;

/**
 * {@link KapuaEntity} attributes.
 * <p>
 * These attributes can be used in the {@link org.eclipse.kapua.model.query.predicate.AttributePredicate} to specify which property
 * to filter {@link org.eclipse.kapua.service.KapuaEntityService#query(KapuaQuery)} and {@link org.eclipse.kapua.service.KapuaEntityService#count(KapuaQuery)} results
 *
 * @since 1.0.0
 */
public class KapuaEntityAttributes {

    protected KapuaEntityAttributes() {
    }

    /**
     * Predicate for field {@link KapuaEntity#getScopeId()}
     *
     * @since 1.0.0
     */
    public static final String SCOPE_ID = "scopeId";

    /**
     * Predicate for field {@link KapuaEntity#getId()}
     *
     * @since 1.0.0
     */
    public static final String ENTITY_ID = "id";

    /**
     * Predicate for field {@link KapuaEntity#getCreatedOn()}
     *
     * @since 1.0.0
     */
    public static final String CREATED_ON = "createdOn";

    /**
     * Predicate for field {@link KapuaEntity#getCreatedBy()}
     *
     * @since 1.0.0
     */
    public static final String CREATED_BY = "createdBy";

}
