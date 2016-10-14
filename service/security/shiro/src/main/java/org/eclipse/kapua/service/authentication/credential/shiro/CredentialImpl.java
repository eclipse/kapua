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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "Credential")
@Table(name = "atht_credential")
/**
 * Credential implementation.
 * 
 * @since 1.0
 *
 */
public class CredentialImpl extends AbstractKapuaUpdatableEntity implements Credential
{
    private static final long serialVersionUID = -7921424688644169175L;

    @XmlElement(name = "userId")
    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "user_id", updatable = false, nullable = false))
    })
    private KapuaEid          userId;

    @XmlElement(name = "credentialType")
    @Enumerated(EnumType.STRING)
    @Column(name = "credential_type", updatable = false, nullable = false)
    private CredentialType    credentialType;

    @XmlElement(name = "credentialKey")
    @Basic
    @Column(name = "credential_key", updatable = false, nullable = false)
    private String            credentialKey;

    /**
     * Constructor
     */
    public CredentialImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     * @param userId
     * @param credentialType
     * @param credentialKey
     */
    public CredentialImpl(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey)
    {
        super(scopeId);
        this.userId = (KapuaEid) userId;
        this.credentialType = credentialType;
        this.credentialKey = credentialKey;
    }

    @Override
    public KapuaId getUserId()
    {
        return userId;
    }

    @Override
    public CredentialType getCredentialType()
    {
        return credentialType;
    }

    @Override
    public String getCredentialKey()
    {
        return credentialKey;
    }

}
