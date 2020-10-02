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
