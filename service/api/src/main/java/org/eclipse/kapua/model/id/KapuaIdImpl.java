/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

public final class KapuaIdImpl implements KapuaId {

    private static final long serialVersionUID = 8660393054811025101L;
    private final BigInteger value;

    public KapuaIdImpl(BigInteger value) {
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
    public String toString() {
        return KapuaId.toString(this);
    }

    @Override
    public int hashCode() {
        return KapuaId.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return KapuaId.areEquals(this, obj);
    }
}
