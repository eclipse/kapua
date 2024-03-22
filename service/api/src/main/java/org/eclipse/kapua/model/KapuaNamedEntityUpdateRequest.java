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
 * {@link KapuaNamedEntityUpdateRequest} definition.
 * <p>
 * The {@link KapuaNamedEntityUpdateRequest} adds on top of the {@link KapuaUpdatableEntityUpdateRequest} the following properties:
 *
 * <ul>
 * <li>description</li>
 * </ul>
 *
 * <div>
 *
 * <p>
 * <b>Description</b>
 * </p>
 * <p>
 * The <i>Description</i> property is the optional description of the {@link KapuaEntity}.
 * </p>
 * </div>
 *
 * @since 1.0.0
 */
public class KapuaNamedEntityUpdateRequest extends KapuaUpdatableEntityUpdateRequest {

    /**
     * The description for the {@link KapuaEntity}
     *
     * @since 2.0.0
     */
    public String description;
}
