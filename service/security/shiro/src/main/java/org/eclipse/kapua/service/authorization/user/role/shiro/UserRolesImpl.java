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
package org.eclipse.kapua.service.authorization.user.role.shiro;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;
import org.eclipse.kapua.service.authorization.user.role.UserRoles;

@Entity(name = "UserRole")
@Table(name = "athz_user_role")
/**
 * User role implementation.
 * 
 * @since 1.0
 *
 */
public class UserRolesImpl extends AbstractKapuaEntity implements UserRoles
{
    private static final long serialVersionUID = -3760818776351242930L;

    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "user_id"))
    })
    private KapuaEid          userId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "athz_user_role_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleImpl>     roles;

    /**
     * Constructor
     */
    protected UserRolesImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public UserRolesImpl(KapuaId scopeId)
    {
        super(scopeId);
    }

    @Override
    public void setUserId(KapuaId userId)
    {
        if (userId.getId() != null) {
            this.userId = new KapuaEid(userId.getId());
        }
    }

    @Override
    public KapuaId getUserId()
    {
        return userId;
    }

    @Override
    public void setRoles(Set<Role> roles)
    {
        Set<RoleImpl> rolesTmp = new HashSet<>();

        for (Role r : roles) {
            RoleImpl role = new RoleImpl(r);
            rolesTmp.add(role);
        }

        this.roles = rolesTmp;
    }

    @Override
    public Set<Role> getRoles()
    {
        Set<Role> rolesTmp = new HashSet<>();

        for (Role r : roles) {
            rolesTmp.add(r);
        }

        return rolesTmp;
    }

}
