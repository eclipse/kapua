/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

import java.util.Date;

/**
 * Credential creator implementation.
 *
 * @since 1.0
 *
 */
public class CredentialCreatorImpl extends AbstractKapuaEntityCreator<Credential> implements CredentialCreator {

    private static final long serialVersionUID = -5020680413729882095L;

    private KapuaId userId;
    private CredentialType credentialType;
    private String credentialKey;
    private Date expirationDate;
    private CredentialStatus credentialStatus;

    /**
     * Constructor
     *
     * @param scopeId
     *            scope identifier
     * @param userId
     *            user identifier
     * @param credentialType
     *            credential type (see {@link CredentialType} for the allowed values)
     * @param credentialKey
     * @param credentialStatus
     * @param expirationDate
     */
    public CredentialCreatorImpl(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey,
            CredentialStatus credentialStatus, Date expirationDate) {
        super(scopeId);

        this.userId = userId;
        this.credentialType = credentialType;
        this.credentialKey = credentialKey;
        this.credentialStatus = credentialStatus;
        this.expirationDate = expirationDate;
    }

    public CredentialCreatorImpl(KapuaId scopeId) {
        super(scopeId);
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
    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public CredentialStatus getCredentialStatus() {
        return credentialStatus;
    }

    @Override
    public void setCredentialStatus(CredentialStatus credentialStatus) {
        this.credentialStatus = credentialStatus;
    }
}
