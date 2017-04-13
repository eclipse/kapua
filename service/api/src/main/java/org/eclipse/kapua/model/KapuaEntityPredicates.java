/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
 *
 */
public interface KapuaEntityPredicates {

    /**
     * {@link KapuaEntity} scopeId.
     */
    public static final String SCOPE_ID = "scopeId";

    /**
     * {@link KapuaEntity} id.
     */
    public static final String ENTITY_ID = "id";

    /**
     * {@link KapuaEntity} created on date.
     */
    public static final String CREATED_ON = "createdOn";

    /**
     * {@link KapuaEntity} created by id.
     */
    public static final String CREATED_BY = "createdBy";

}
