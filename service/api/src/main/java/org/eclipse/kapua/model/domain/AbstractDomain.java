/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
                Objects.equals(getServiceName(), domain.getServiceName()) &&
                Objects.equals(getActions(), domain.getActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getServiceName(), getActions(), getGroupable());
    }
}
