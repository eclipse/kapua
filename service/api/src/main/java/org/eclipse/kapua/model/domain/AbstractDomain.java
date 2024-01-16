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

public abstract class AbstractDomain implements Domain {

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Domain)) {
            return false;
        }

        final Domain that = (Domain) object;

        if (getGroupable() != that.getGroupable()) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (!getActions().equals(that.getActions())) {
            return false;
        }
        return getServiceName() != null ? getServiceName().equals(that.getServiceName()) : that.getServiceName() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getGroupable() ? 1 : 0);
        result = 31 * result + getActions().hashCode();
        result = 31 * result + (getServiceName() != null ? getServiceName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DomainEntry{" +
                "name='" + getName() + '\'' +
                ", groupable=" + getGroupable() +
                ", actions=" + getActions() +
                ", serviceName='" + getServiceName() + '\'' +
                '}';
    }
}
