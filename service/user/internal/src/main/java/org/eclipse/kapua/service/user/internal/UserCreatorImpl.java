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

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;

/**
 * User creator service implementation.
 * 
 * @since 1.0
 *
 */
public class UserCreatorImpl extends AbstractKapuaNamedEntityCreator<User> implements UserCreator
{
    private static final long serialVersionUID = 4664940282892151008L;

    private String            displayName;
    private String            email;
    private String            phoneNumber;

    /**
     * Constructor
     * 
     * @param accountId
     * @param name
     */
    public UserCreatorImpl(KapuaId accountId, String name)
    {
        super(accountId, name);
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
}
