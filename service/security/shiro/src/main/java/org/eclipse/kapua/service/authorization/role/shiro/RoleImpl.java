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
package org.eclipse.kapua.service.authorization.role.shiro;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;

/**
 * {@link Role} implementation.
 * 
 * @since 1.0
 */
@Entity(name = "Role")
@Table(name = "athz_role")
public class RoleImpl extends AbstractKapuaUpdatableEntity implements Role {

    private static final long serialVersionUID = -3760818776351242930L;

    @Basic
    @Column(name = "name")
    private String name;

    protected RoleImpl() {
        super();
    }

    /**
     * Constructor.<br>
     * Creates a soft clone.
     * 
     * @param role
     * @throws KapuaException
     */
    public RoleImpl(Role role) throws KapuaException {
        super((AbstractKapuaUpdatableEntity) role);

        setName(role.getName());
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public RoleImpl(KapuaId scopeId) {
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        RoleImpl other = (RoleImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
