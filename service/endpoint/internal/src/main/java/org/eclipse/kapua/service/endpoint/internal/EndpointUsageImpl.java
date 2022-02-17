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
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.service.endpoint.EndpointUsage;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class EndpointUsageImpl implements EndpointUsage {

    @Basic
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    public EndpointUsageImpl(String name) {
        setName(name);
    }

    public EndpointUsageImpl(EndpointUsage endpointUsage) {
        setName(endpointUsage.getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public EndpointUsageImpl() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndpointUsageImpl that = (EndpointUsageImpl) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static EndpointUsageImpl parse(EndpointUsage endpointUsage) {
        return endpointUsage != null ? endpointUsage instanceof EndpointUsageImpl ? (EndpointUsageImpl) endpointUsage : new EndpointUsageImpl(endpointUsage) : null;
    }
}
