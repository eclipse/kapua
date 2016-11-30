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
package org.eclipse.kapua.service.user.internal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

/**
 * User entity implementation.
 * 
 * @since 1.0
 *
 */
@Entity(name = "User")
@Table(name = "usr_user")
public class UserImpl extends AbstractKapuaNamedEntity implements User
{
    private static final long serialVersionUID = 4029650117581681503L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus        status;

    @Basic
    @Column(name = "display_name")
    private String            displayName;

    @Basic
    @Column(name = "email")
    private String            email;

    @Basic
    @Column(name = "phone_number")
    private String            phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", updatable = false)
    private UserType          userType;
    
    @Basic
    @Column(name = "external_id", updatable = false)
    private String            externalId;

    /**
     * Constructor
     */
    public UserImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     * @param name
     */
    public UserImpl(KapuaId scopeId,
                    String name)
    {
        super(scopeId, name);
        this.status = UserStatus.ENABLED;
    }

    @Override
    public UserStatus getStatus()
    {
        return status;
    }

    @Override
    public void setStatus(UserStatus status)
    {
        this.status = status;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String getEmail()
    {
        return email;
    }

    @Override
    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public UserType getUserType() {
        return userType;
    }

    @Override
    public void setUserType(UserType userType) {
        this.userType = userType;
        
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
