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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
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
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id", updatable = false, nullable = false))
    })
    private KapuaEid userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "credential_type", updatable = false, nullable = false)
    private CredentialType credentialType;

    @Basic
    @Column(name = "credential_key", nullable = false)
    private String credentialKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_date")
    protected Date expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "credential_status", nullable = false)
    private CredentialStatus status;

    @Basic
    @Column(name = "login_failures", nullable = false)
    private int loginFailures;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "first_login_failure")
    protected Date firstLoginFailure;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_failures_reset")
    protected Date loginFailuresReset;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lockout_reset")
    protected Date lockoutReset;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public CredentialImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link Credential}.
     * @since 1.0.0
     */
    public CredentialImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     *
     * @param scopeId        The scope {@link KapuaId} to set into the {@link Credential}.
     * @param userId         The {@link org.eclipse.kapua.service.user.User} {@link KapuaId} to set into the {@link Credential}.
     * @param credentialType The {@link CredentialType} to set into the {@link Credential}.
     * @param credentialKey  The credential key to set into the {@link Credential}.
     * @since 1.0.0
     */
    public CredentialImpl(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        super(scopeId);
        this.userId = (KapuaEid) userId;
        this.credentialType = credentialType;
        this.credentialKey = credentialKey;
        this.status = credentialStatus;
        this.expirationDate = expirationDate;
    }

    /**
     * Clone constructor.
     *
     * @param credential
     * @throws KapuaException
     * @since 1.1.0
     */
    public CredentialImpl(Credential credential) throws KapuaException {
        super(credential);

        setUserId(credential.getUserId());
        setCredentialType(credential.getCredentialType());
        setCredentialKey(credential.getCredentialKey());
        setExpirationDate(credential.getExpirationDate());
        setStatus(credential.getStatus());
        setLoginFailures(credential.getLoginFailures());
        setLoginFailuresReset(credential.getLoginFailuresReset());
        setLockoutReset(credential.getLockoutReset());
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public CredentialType getCredentialType() {
        return credentialType;
    }

    @Override
    public String getCredentialKey() {
        return credentialKey;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = KapuaEid.parseKapuaId(userId);
    }

    @Override
    public void setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
    }

    @Override
    public void setCredentialKey(String credentialKey) {
        this.credentialKey = credentialKey;
    }

    @Override
    public CredentialStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(CredentialStatus status) {
        this.status = status;
    }

    @Override
    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setUserId(KapuaEid userId) {
        this.userId = userId;
    }

    @Override
    public int getLoginFailures() {
        return loginFailures;
    }

    @Override
    public void setLoginFailures(int loginFailures) {
        this.loginFailures = loginFailures;
    }

    @Override
    public Date getFirstLoginFailure() {
        return firstLoginFailure;
    }

    @Override
    public void setFirstLoginFailure(Date firstLoginFailure) {
        this.firstLoginFailure = firstLoginFailure;
    }

    @Override
    public Date getLoginFailuresReset() {
        return loginFailuresReset;
    }

    @Override
    public void setLoginFailuresReset(Date loginFailuresReset) {
        this.loginFailuresReset = loginFailuresReset;
    }

    @Override
    public Date getLockoutReset() {
        return lockoutReset;
    }

    @Override
    public void setLockoutReset(Date lockoutReset) {
        this.lockoutReset = lockoutReset;
    }
}
