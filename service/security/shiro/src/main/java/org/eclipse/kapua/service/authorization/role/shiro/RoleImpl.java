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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.user.role.shiro.UserRolesImpl;

@Entity(name = "Role")
@Table(name = "athz_role")
/**
 * Role implementation.
 * 
 * @since 1.0
 *
 */
public class RoleImpl extends AbstractKapuaUpdatableEntity implements Role {

    private static final long serialVersionUID = -3760818776351242930L;

    @ManyToMany(mappedBy = "roles")
    private Set<UserRolesImpl> roles;

    @XmlElement(name = "name")
    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Set<RolePermissionImpl> permissions;

    protected RoleImpl() {
        super();
    }

    /**
     * Constructor.<br>
     * Creates a soft clone.
     * 
     * @param role
     */
    public RoleImpl(Role role) {
        super(role.getScopeId());
        id = (KapuaEid) role.getId();
        createdOn = role.getCreatedOn();
        createdBy = (KapuaEid) role.getCreatedBy();
        setName(role.getName());
        setPermissions(role.getPermissions());
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
    public void setPermissions(Set<RolePermission> permissions) {
        this.permissions = new HashSet<>();

        for (RolePermission p : permissions) {

            RolePermissionImpl rp = new RolePermissionImpl(p.getScopeId(),
                    p.getPermission().getDomain(),
                    p.getPermission().getAction(),
                    p.getPermission().getTargetScopeId());

            this.permissions.add(rp);
        }
    }

    @Override
    public Set<RolePermission> getPermissions() {
        Set<RolePermission> permissionsTmp = new HashSet<>();

        for (RolePermissionImpl p : permissions) {
            permissionsTmp.add(p);
        }

        return permissionsTmp;
    }

}
