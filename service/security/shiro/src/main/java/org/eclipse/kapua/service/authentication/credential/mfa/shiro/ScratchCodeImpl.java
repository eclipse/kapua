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
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity(name = "ScratchCode")
@Table(name = "atht_scratch_code")
/**
 * {@link ScratchCode} implementation.
 */
public class ScratchCodeImpl extends AbstractKapuaUpdatableEntity implements ScratchCode {

    private static final long serialVersionUID = -2785931432917205759L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "mfa_credential_option_id", updatable = false, nullable = false))
    })
    private KapuaEid mfaCredentialOptionId;

    @Basic
    @Column(name = "scratch_code", nullable = false)
    private String code;

    /**
     * Constructor.
     */
    public ScratchCodeImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link MfaCredentialOption}.
     */
    public ScratchCodeImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link MfaCredentialOption}.
     * @param code    The credential key to set into the {@link MfaCredentialOption}.
     */
    public ScratchCodeImpl(KapuaId scopeId, KapuaId mfaCredentialOptionId, String code) {
        super(scopeId);
        this.mfaCredentialOptionId = (KapuaEid) mfaCredentialOptionId;
        this.code = code;
    }

    /**
     * Clone constructor.
     *
     * @param scratchCode
     * @throws KapuaException
     */
    public ScratchCodeImpl(ScratchCode scratchCode) throws KapuaException {
        super(scratchCode);
        setCode(scratchCode.getCode());
        setMfaCredentialOptionId(scratchCode.getMfaCredentialOptionId());
    }

    @Override
    public KapuaId getMfaCredentialOptionId() {
        return this.mfaCredentialOptionId;
    }

    @Override
    public void setMfaCredentialOptionId(KapuaId mfaCredentialOptionId) {
        this.mfaCredentialOptionId = KapuaEid.parseKapuaId(mfaCredentialOptionId);
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
