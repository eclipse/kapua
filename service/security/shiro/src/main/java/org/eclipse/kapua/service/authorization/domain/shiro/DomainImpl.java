/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain.shiro;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;

/**
 * {@link Domain} implementation.
 * 
 * @since 1.0
 */
@Entity(name = "Domain")
@Table(name = "athz_domain")
// @Embeddable
public class DomainImpl extends AbstractKapuaEntity implements Domain {

    private static final long serialVersionUID = 3878607249074632729L;

    @Basic
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Basic
    @Column(name = "serviceName", nullable = false, updatable = false)
    private String serviceName;

    @ElementCollection
    @CollectionTable(name = "athz_domain_actions", joinColumns = @JoinColumn(name = "domain_id", referencedColumnName = "id"))
    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Actions> actions;

    /**
     * Constructor
     */
    public DomainImpl() {
        super();
    }

    public DomainImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DomainImpl other = (DomainImpl) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (serviceName == null) {
            if (other.serviceName != null)
                return false;
        } else if (!serviceName.equals(other.serviceName))
            return false;
        return true;
    }
}
