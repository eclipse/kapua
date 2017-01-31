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
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.eclipse.kapua.model.subject.SubjectType;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;

/**
 * Username and password {@link AuthenticationCredentials} implementation.
 * 
 * @since 1.0
 * 
 */
public class UsernamePasswordCredentialsImpl extends UsernamePasswordToken implements UsernamePasswordCredentials, AuthenticationToken {

    private static final long serialVersionUID = -7549848672967689716L;

    private SubjectType subjectType;
    private String username;
    private char[] password;

    /**
     * Constructor.
     * 
     * @param username
     * @param password
     */
    public UsernamePasswordCredentialsImpl(SubjectType subjectType, String username, char[] password) {
        setSubjectType(subjectType);
        setUsername(username);
        setPassword(password);
    }

    @Override
    public SubjectType getSubjectType() {
        return subjectType;
    }

    @Override
    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public char[] getPassword() {
        return password;
    }

    @Override
    public void setPassword(char[] password) {
        this.password = password;
    }
}
