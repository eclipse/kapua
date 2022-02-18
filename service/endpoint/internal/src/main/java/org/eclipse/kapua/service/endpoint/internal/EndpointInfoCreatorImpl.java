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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointUsage;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link EndpointInfoCreator} implementation.
 *
 * @since 1.0.0
 */
public class EndpointInfoCreatorImpl extends AbstractKapuaEntityCreator<EndpointInfo> implements EndpointInfoCreator {

    private String schema;
    private String dns;
    private int port;
    private boolean secure;
    private Set<EndpointUsage> usages;
    private String endpointType;

    protected EndpointInfoCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String getDns() {
        return dns;
    }

    @Override
    public void setDns(String dns) {
        this.dns = dns;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean getSecure() {
        return secure;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public Set<EndpointUsage> getUsages() {
        if (usages == null) {
            usages = new HashSet<>();
        }

        return usages;
    }

    @Override
    public void setUsages(Set<EndpointUsage> usages) {
        this.usages = usages;
    }

    @Override
    public String getEndpointType() {
        return endpointType;
    }

    @Override
    public void setEndpointType(String endpointType) {
        this.endpointType = endpointType;
    }

}
