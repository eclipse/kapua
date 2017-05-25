/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        KapuaIdStatic other = (KapuaIdStatic) obj;
        if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

}