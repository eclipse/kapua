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
package org.eclipse.kapua.service.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntity;

/**
 * User entity
 * 
 * @since 1.0
 * 
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "status",
                       "displayName",
                       "email",
                       "phoneNumber",
                       "userType",
                       "externalId"
                     },
         factoryClass = UserXmlRegistry.class, 
         factoryMethod = "newUser")
public interface User extends KapuaNamedEntity
{
    public static final String TYPE = "user";

    default public String getType()
    {
        return TYPE;
    }

    /**
     * Return the user status
     * 
     * @return
     */
    @XmlElement(name = "status")
    public UserStatus getStatus();

    /**
     * Get the user status
     * 
     * @param status
     */
    public void setStatus(UserStatus status);

    /**
     * Return the display name (may be a friendly username to show in the UI)
     * 
     * @return
     */
    @XmlElement(name = "displayName")
    public String getDisplayName();

    /**
     * Set the display name
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName);

    /**
     * Get the user email
     * 
     * @return
     */
    @XmlElement(name = "email")
    public String getEmail();

    /**
     * Set the user email
     * 
     * @param email
     */
    public void setEmail(String email);

    /**
     * Get the phone number
     * 
     * @return
     */
    @XmlElement(name = "phoneNumber")
    public String getPhoneNumber();

    /**
     * Set the phone number
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber);
    
    /**
     * Get the user type
     * 
     * @return
     */
    @XmlElement(name = "userType")
    public UserType getUserType();

    /**
     * Set the user type
     * 
     * @param phoneNumber
     */
    public void setUserType(UserType userType);
    
    /**
     * Get the external ID
     * 
     * @return
     */
    @XmlElement(name = "externalId")
    public String getExternalId();

    /**
     * Set the external ID
     * 
     * @param externalId
     */
    public void setExternalId(String externalId);
}
