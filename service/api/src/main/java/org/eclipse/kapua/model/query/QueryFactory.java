/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link KapuaQuery} {@link KapuaObjectFactory} definition.
 *
 * @since 2.0.0
 */
public interface QueryFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link KapuaQuery}.
     *
     * @return The newly instantiated {@link KapuaQuery}.
     * @since 2.0.0
     */
    KapuaQuery newQuery();
}
