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

/**
 * {@link KapuaEntity} query predicates.
 *
 * @since 1.0.0
 */
public interface KapuaEntityPredicates extends KapuaPredicates {

    /**
     * Predicate for field {@link KapuaEntity#getScopeId()}
     *
     * @since 1.0.0
     */
    String SCOPE_ID = "scopeId";

    /**
     * Predicate for field {@link KapuaEntity#getId()}
     *
     * @since 1.0.0
     */
    String ENTITY_ID = "id";

    /**
     * Predicate for field {@link KapuaEntity#getCreatedOn()}
     *
     * @since 1.0.0
     */
    String CREATED_ON = "createdOn";

    /**
     * Predicate for field {@link KapuaEntity#getCreatedBy()}
     *
     * @since 1.0.0
     */
    String CREATED_BY = "createdBy";

    String MODIFIED_ON = "modifiedOn";

}
