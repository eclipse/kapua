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
 *
 *******************************************************************************/
package org.eclipse.kapua.model;

import org.eclipse.kapua.model.subject.Subject;

/**
 * {@link KapuaUpdatableEntity} query predicates.
 * 
 * @since 1.0.0
 *
 */
public interface KapuaUpdatableEntityPredicates extends KapuaEntityPredicates {

    /**
     * {@link KapuaUpdatableEntity} modified on date.
     */
    public static final String MODIFIED_ON = "modifiedOn";

    /**
     * {@link KapuaUpdatableEntity} modified by {@link Subject}.
     */
    public static final String MODIFIED_BY = "modifiedBy";

}
