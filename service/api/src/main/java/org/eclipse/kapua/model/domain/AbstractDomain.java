/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.domain;

import java.util.Objects;

public abstract class AbstractDomain implements Domain {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Domain domain = (Domain) o;
        return getGroupable() == domain.getGroupable() &&
                Objects.equals(getName(), domain.getName()) &&
                Objects.equals(getActions(), domain.getActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getActions(), getGroupable());
    }
}
