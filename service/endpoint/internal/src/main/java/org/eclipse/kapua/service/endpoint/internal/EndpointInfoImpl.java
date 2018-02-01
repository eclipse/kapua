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
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointUsage;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link EndpointInfo} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "EndpointInfo")
@Table(name = "endp_endpoint_info")
public class EndpointInfoImpl extends AbstractKapuaUpdatableEntity implements EndpointInfo {

    @Basic
    @Column(name = "schema", updatable = true, nullable = false)
    private String schema;

    @Basic
    @Column(name = "dns", updatable = true, nullable = false)
    private String dns;

    @Basic
    @Column(name = "port", updatable = true, nullable = false)
    private int port;

    @Basic
    @Column(name = "secure", updatable = true, nullable = false)
    private boolean secure;

    @ElementCollection
    @CollectionTable(name = "endp_endpoint_info_usage", joinColumns = @JoinColumn(name = "endpoint_info_id", referencedColumnName = "id"))
    private Set<EndpointUsageImpl> usages;

    protected EndpointInfoImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public EndpointInfoImpl(KapuaId scopeId) {
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
    public Set<EndpointUsageImpl> getUsages() {
        if (usages == null) {
            usages = new HashSet<>();
        }

        return usages;
    }

    @Override
    public void setUsages(Set<EndpointUsage> usages) {
        this.usages = new HashSet<>();

        usages.forEach(u -> this.usages.add(EndpointUsageImpl.parse(u)));
    }
}
