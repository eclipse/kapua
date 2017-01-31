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
package org.eclipse.kapua.service.authentication.credential.shiro;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.subject.SubjectImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "Credential")
@Table(name = "atht_credential")
/**
 * {@link Credential} implementation.
 * 
 * @since 1.0.0
 */
public class CredentialImpl extends AbstractKapuaUpdatableEntity implements Credential {

    private static final long serialVersionUID = -7921424688644169175L;

    @Embedded
    private SubjectImpl subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false)
    private CredentialType type;

    @Basic
    @Column(name = "key", updatable = false, nullable = false)
    private String key;

    @Basic
    @Column(name = "secret", updatable = false, nullable = false)
    private String secret;

    /**
     * Constructor
     */
    public CredentialImpl() {
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
    public CredentialImpl(KapuaId scopeId,//
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
            this.subject = new SubjectImpl(subject);
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
