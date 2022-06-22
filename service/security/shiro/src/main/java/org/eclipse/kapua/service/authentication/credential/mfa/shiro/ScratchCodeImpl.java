/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

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

/**
 * {@link ScratchCode} implementation.
 *
 * @since 1.3.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity(name = "ScratchCode")
@Table(name = "atht_scratch_code")
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
     *
     * @since 1.3.0
     */
    public ScratchCodeImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link ScratchCode#getScopeId()}.
     * @since 1.3.0
     */
    public ScratchCodeImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link ScratchCode}.
     * @param code    The code to set into the {@link ScratchCode}.
     * @since 1.3.0
     */
    public ScratchCodeImpl(KapuaId scopeId, KapuaId mfaOptionId, String code) {
        super(scopeId);

        setMfaOptionId(KapuaEid.parseKapuaId(mfaOptionId));
        setCode(code);
    }

    /**
     * Clone constructor.
     *
     * @param scratchCode The {@link ScratchCode} to clone.
     * @since 1.3.0
     */
    public ScratchCodeImpl(ScratchCode scratchCode) {
        super(scratchCode);

        setMfaOptionId(scratchCode.getMfaOptionId());
        setCode(scratchCode.getCode());
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
