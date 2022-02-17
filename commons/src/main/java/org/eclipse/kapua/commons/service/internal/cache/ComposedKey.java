/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.service.internal.cache;

import org.eclipse.kapua.model.id.KapuaId;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is needed to define the keys for the {@link EntityCache#listsCache}.
 */
public class ComposedKey implements Serializable {

    private KapuaId scopeId;
    private Serializable key;

    public ComposedKey(KapuaId scopeId, Serializable key) {
        this.scopeId = scopeId;
        this.key = key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scopeId, key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ComposedKey)) {
            return false;
        }
        ComposedKey otherCacheKey = (ComposedKey) obj;
        return Objects.equals(scopeId, otherCacheKey.getScopeId()) && Objects.equals(key,
                otherCacheKey.getKey());
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public Serializable getKey() {
        return key;
    }

}
