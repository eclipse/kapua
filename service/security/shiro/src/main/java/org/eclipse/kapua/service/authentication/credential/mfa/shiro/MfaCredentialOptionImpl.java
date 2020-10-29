/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOption;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity(name = "MfaCredentialOption")
@Table(name = "atht_mfa_credential_option")
/**
 * {@link MfaCredentialOption} implementation.
 */
public class MfaCredentialOptionImpl extends AbstractKapuaUpdatableEntity implements MfaCredentialOption {

    private static final long serialVersionUID = -1872939877726584407L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id", updatable = false, nullable = false))
    })
    private KapuaEid userId;

    @Basic
    @Column(name = "mfa_credential_key", nullable = false)
    private String mfaCredentialKey;

    @Basic
    @Column(name = "trust_key")
    private String trustKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "trust_expiration_date")
    protected Date trustExpirationDate;


    /**
     * Constructor.
     */
    public MfaCredentialOptionImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link MfaCredentialOption}.
     */
    public MfaCredentialOptionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     *
     * @param scopeId          The scope {@link KapuaId} to set into the {@link MfaCredentialOption}.
     * @param userId           user identifier
     * @param mfaCredentialKey The credential key to set into the {@link MfaCredentialOption}.
     */
    public MfaCredentialOptionImpl(KapuaId scopeId, KapuaId userId, String mfaCredentialKey) {
        super(scopeId);
        this.userId = (KapuaEid) userId;
        this.mfaCredentialKey = mfaCredentialKey;
    }

    /**
     * Clone constructor.
     *
     * @param credential
     * @throws KapuaException
     */
    public MfaCredentialOptionImpl(MfaCredentialOption credential) throws KapuaException {
        super(credential);
        setUserId(credential.getUserId());
        setMfaCredentialKey(credential.getMfaCredentialKey());
        setTrustKey(credential.getTrustKey());
        setTrustExpirationDate(credential.getTrustExpirationDate());
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = KapuaEid.parseKapuaId(userId);
    }

    @Override
    public String getMfaCredentialKey() {
        return mfaCredentialKey;
    }

    @Override
    public void setMfaCredentialKey(String mfaCredentialKey) {
        this.mfaCredentialKey = mfaCredentialKey;
    }

    @Override
    public String getTrustKey() {
        return this.trustKey;
    }

    @Override
    public void setTrustKey(String trustKey) {
        this.trustKey = trustKey;
    }

    @Override
    public Date getTrustExpirationDate() {
        return trustExpirationDate;
    }

    @Override
    public void setTrustExpirationDate(Date trustExpirationDate) {
        this.trustExpirationDate = trustExpirationDate;
    }

}
