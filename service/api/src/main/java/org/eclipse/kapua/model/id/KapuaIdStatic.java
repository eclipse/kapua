/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.id;

import java.math.BigInteger;

public final class KapuaIdStatic implements KapuaId {

    private static final long serialVersionUID = 8660393054811025101L;
    private final BigInteger value;

    public KapuaIdStatic(BigInteger value) {
        if (null == value) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    @Override
    public BigInteger getId() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass() && !(obj instanceof KapuaId)) {
            return false;
        }
        KapuaId other = (KapuaId) obj;
        if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

}
