/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
 * {@link KapuaUpdatableEntity} attributes.
 *
 * @see org.eclipse.kapua.model.KapuaEntityAttributes
 * @since 1.0.0
 */
public class KapuaUpdatableEntityAttributes extends KapuaEntityAttributes {

    /**
     * {@link KapuaUpdatableEntity} modified on date.
     */
    public static final String MODIFIED_ON = "modifiedOn";

    /**
     * {@link KapuaUpdatableEntity} modified by id.
     */
    public static final String MODIFIED_BY = "modifiedBy";

}
