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
package org.eclipse.kapua.service.authorization.permission.shiro;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
/**
 * Permission implementation.
 * 
 * @since 1.0
 *
 */
public class PermissionImpl implements Permission, Serializable {

    private static final long serialVersionUID = 1480557438886065675L;

    @Basic
    @Column(name = "domain", nullable = false, updatable = false)
    private String domain;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, updatable = false)
    private Actions action;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "target_scope_id", updatable = false))
    })
    private KapuaEid targetScopeId;

    protected PermissionImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param domain
     * @param action
     * @param targetScopeId
     */
    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId) {
        this();
        this.domain = domain;
        this.action = action;
        if (targetScopeId != null) {
            this.targetScopeId = new KapuaEid(targetScopeId.getId());
        }
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setAction(Actions action) {
        this.action = action;
    }

    @Override
    public Actions getAction() {
        return action;
    }

    @Override
    public void setTargetScopeId(KapuaId targetScopeId) {
        this.targetScopeId = new KapuaEid(targetScopeId.getId());
    }

    @Override
    public KapuaId getTargetScopeId() {
        return targetScopeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(domain);

        if (action != null) {
            sb.append(":")
                    .append(action.name());
        }
        if (targetScopeId != null) {
            sb.append(":")
                    .append(targetScopeId.getId());
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + ((targetScopeId == null) ? 0 : targetScopeId.hashCode());
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
        PermissionImpl other = (PermissionImpl) obj;
        if (action != other.action)
            return false;
        if (domain == null) {
            if (other.domain != null)
                return false;
        } else if (!domain.equals(other.domain))
            return false;
        if (targetScopeId == null) {
            if (other.targetScopeId != null)
                return false;
        } else if (!targetScopeId.equals(other.targetScopeId))
            return false;
        return true;
    }
}
