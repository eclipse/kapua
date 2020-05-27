/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
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
            @AttributeOverride(name = "eid", column = @Column(name = "mfa_option_id", updatable = false, nullable = false))
    })
    private KapuaEid mfaOptionId;

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
     * @param scopeId The scope {@link KapuaId} to set into the {@link ScratchCode}.
     */
    public ScratchCodeImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link ScratchCode}.
     * @param code    The code to set into the {@link ScratchCode}.
     */
    public ScratchCodeImpl(KapuaId scopeId, KapuaId mfaOptionId, String code) {
        super(scopeId);
        this.mfaOptionId = (KapuaEid) mfaOptionId;
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
        setMfaOptionId(scratchCode.getMfaOptionId());
    }

    @Override
    public KapuaId getMfaOptionId() {
        return this.mfaOptionId;
    }

    @Override
    public void setMfaOptionId(KapuaId mfaOptionId) {
        this.mfaOptionId = KapuaEid.parseKapuaId(mfaOptionId);
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
