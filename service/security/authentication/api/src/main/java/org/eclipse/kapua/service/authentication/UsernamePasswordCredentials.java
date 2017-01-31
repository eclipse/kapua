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
package org.eclipse.kapua.service.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.subject.SubjectType;

/**
 * Username and password {@link LoginCredentials} definition.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "usernamePasswordCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "username", "password" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newUsernamePasswordCredentials")
public interface UsernamePasswordCredentials extends LoginCredentials {

    /**
     * Returns the {@link CredentialSubjectType}.
     * 
     * @return The {@link CredentialSubjectType}.
     * @since 1.0.0
     */
    @XmlTransient
    public SubjectType getSubjectType();

    /**
     * Sets the {@link CredentialSubjectType}.
     * 
     * @param subjectType
     *            The {@link CredentialSubjectType}.
     * @since 1.0.0
     */
    public void setSubjectType(SubjectType subjectType);

    /**
     * Returns the username.
     * 
     * @return The username.
     * @since 1.0.0
     */
    @XmlElement(name = "username")
    public String getUsername();

    /**
     * Set the username
     * 
     * @param username
     * @since 1.0.0
     */
    public void setUsername(String username);

    /**
     * Returns the password.
     * 
     * @return The password.
     * @since 1.0.0
     */
    @XmlJavaTypeAdapter(StringToCharArrayAdapter.class)
    @XmlElement(name = "password")
    public char[] getPassword();

    /**
     * Sets the password.
     * 
     * @param password
     *            The password.
     * @since 1.0.0
     */
    public void setPassword(char[] password);
}
