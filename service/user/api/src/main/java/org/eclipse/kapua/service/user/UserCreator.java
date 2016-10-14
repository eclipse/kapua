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

import org.eclipse.kapua.model.KapuaNamedEntityCreator;

/**
 * UserCreator encapsulates all the information needed to create a new User in the system.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name="userCreator")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "displayName",
                       "email",
                       "phoneNumber" },
         factoryClass = UserXmlRegistry.class,
         factoryMethod = "newUserCreator")
public interface UserCreator extends KapuaNamedEntityCreator<User>
{

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
     * Get the email
     * 
     * @return
     */
    @XmlElement(name = "email")
    public String getEmail();

    /**
     * Set the email
     * 
     * @param email
     */
    public void setEmail(String email);

    /**
     * Get the phine number
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
}
