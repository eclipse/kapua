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
package org.eclipse.kapua.test.authentication;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authorization.subject.Subject;
import org.eclipse.kapua.test.authorization.subject.SubjectMock;

/**
 * {@link Credential} implementation.
 * 
 * @since 1.0.0
 *
 */
public class CredentialMock extends AbstractKapuaUpdatableEntity implements Credential {

    private static final long serialVersionUID = -7921424688644169175L;

    @Embedded
    private SubjectMock subject;
    private CredentialType type;
    private String key;

    @Basic
    @Column(name = "secret", updatable = false, nullable = false)
    private String secret;

    /**
     * Constructor
     */
    public CredentialMock() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param scopeId
     * @param subject
     * @param type
     * @param key
     * @param cryptedSecret
     */
    public CredentialMock(KapuaId scopeId,//
            Subject subject, //
            CredentialType type,//
            String key,//
            String cryptedSecret) //
    {
        super(scopeId);
        setSubject(subject);
        setCredentialType(type);
        setKey(key);
        setSecret(cryptedSecret);
    }

    @Override
    public Subject getSubject() {
        return subject;
    }

    @Override
    public void setSubject(Subject subject) {
        if (subject != null) {
            this.subject = new SubjectMock(subject);
        } else {
            this.subject = null;
        }
    }

    @Override
    public CredentialType getCredentialType() {
        return type;
    }

    @Override
    public void setCredentialType(CredentialType credentialType) {
        this.type = credentialType;

    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String credentialKey) {
        this.key = credentialKey;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public void setSecret(String secret) {
        this.secret = secret;
    }

}
