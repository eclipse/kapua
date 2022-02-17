/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
