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

import java.util.Objects;

/**
 * {@link StorableId} implementation.
 *
 * @since 1.0.0
 */
public class StorableIdImpl implements StorableId {

    private final String id;

    /**
     * Constructor.
     *
     * @param id The {@link StorableId} in {@link String} form.
     * @since 1.0.0
     */
    public StorableIdImpl(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StorableIdImpl other = (StorableIdImpl) obj;
        if (id == null) {
            return other.id == null;
        }

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
