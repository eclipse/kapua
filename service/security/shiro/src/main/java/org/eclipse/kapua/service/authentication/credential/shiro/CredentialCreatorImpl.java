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

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialSubject;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

/**
 * Credential creator implementation.
 * 
 * @since 1.0
 *
 */
public class CredentialCreatorImpl extends AbstractKapuaEntityCreator<Credential> implements CredentialCreator {

    private static final long serialVersionUID = -5020680413729882095L;

    @XmlElement(name = "userId")
    private KapuaId userId;

    @XmlElement(name = "credentialType")
    private CredentialType credentialType;

    @XmlElement(name = "credentialKey")
    private String            credentialKey;
    
    @XmlElement(name = "credentialSubject")
    private CredentialSubject credentialSubject;
    
    @XmlElement(name = "credentialSubjectId")
    private KapuaId           credentialSubjectId;

    /**
     * Constructor
     * 
     * @param scopeId scope identifier
     * @param userId user identifier
     * @param credentialType credential type (see {@link CredentialType} for the allowed values)
     * @param credentialKey credential key
     * @param credentialSubject credential subject (see {@link CredentialSubject} for the allowed values)
     * @param credentialSubjectId credential subject id
     */
    public CredentialCreatorImpl(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialSubject credentialSubject, KapuaId credentialSubjectId)
    {
        super(scopeId);

        this.userId = userId;
        this.credentialType = credentialType;
        this.credentialKey = credentialKey;
        this.credentialSubject = credentialSubject;
        this.credentialSubjectId = credentialSubjectId;
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }
    
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }

    @Override
    public CredentialType getCredentialType() {
        return credentialType;
    }
    
    public void setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
    }

    @Override
    public String getCredentialPlainKey() {
        return credentialKey;
    }
    
    @Override
    public void setCredentialPlainKey(String credentialKey) {
        this.credentialKey = credentialKey;
    }

    @Override
    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    @Override
    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    @Override
    public KapuaId getCredentialSubjectId() {
        return credentialSubjectId;
    }

    @Override
    public void setCredentialSubjectId(KapuaId credentialSubjectId) {
        this.credentialSubjectId = credentialSubjectId;
    }
}
