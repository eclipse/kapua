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
package org.eclipse.kapua.service.storable.model.id;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.service.storable.model.Storable;

/**
 * {@link Storable} identifier definition.
 * <p>
 * It defines the identifier of every {@link Storable}s.
 *
 * @since 1.0.0
 */
public interface StorableId extends KapuaSerializable {

    /**
     * Return the {@link StorableId} in {@link String} form.
     *
     * @return The {@link StorableId} in {@link String} form.
     * @since 1.0.0
     */
    @Override
    String toString();
}
