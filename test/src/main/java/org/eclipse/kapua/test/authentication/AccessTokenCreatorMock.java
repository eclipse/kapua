/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import java.util.Date;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.SubjectType;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;

/**
 * Access token implementation
 * 
 * @since 1.0
 *
 */
public class AccessTokenCreatorMock extends AbstractKapuaEntityCreator<AccessToken> implements AccessTokenCreator {

    private static final long serialVersionUID = -27718046815190710L;

    private SubjectType subjectType;
    private KapuaId subjectId;
    private KapuaId credentialId;
    private String tokenId;
    private Date expiresOn;

    /**
     * Constructor
     * 
     * @param scopeId
     */
    protected AccessTokenCreatorMock(KapuaId scopeId) {
        super(scopeId);
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
    public KapuaId getSubjectId() {
        return subjectId;
    }

    @Override
    public void setSubjectId(KapuaId subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public KapuaId getCredentialId() {
        return credentialId;
    }

    @Override
    public void setCredentialId(KapuaId credentialId) {
        this.credentialId = credentialId;
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;

    }

    @Override
    public Date getExpiresOn() {
        return expiresOn;
    }

    @Override
    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

}
