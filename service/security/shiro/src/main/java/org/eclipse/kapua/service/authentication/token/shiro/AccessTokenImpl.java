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
package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * Access token entity implementation.
 * 
 * @since 1.0
 * 
 */
public class AccessTokenImpl extends AbstractKapuaUpdatableEntity implements AccessToken {

    private static final long serialVersionUID = -6003387376828196787L;

    @XmlElement(name = "userId")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id", updatable = false, nullable = false))
    })
    private KapuaId userId;

    @XmlElement(name = "tokenId")
    @Basic
    @Column(name = "tokenId", updatable = false, nullable = false)
    private String tokenId;

    @XmlElement(name = "expiresOn")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiresOn", nullable = false)
    private Date expiresOn;

    /**
     * Constructor
     * 
     * @param userId
     *            user identifier
     * @param scopeId
     *            scope identifier
     * @param tokenId
     *            token identifier
     * @param expiresOn
     *            token expiration date
     */
    public AccessTokenImpl(KapuaId scopeId, KapuaId userId, String tokenId, Date expiresOn) {
        super(scopeId);
        this.userId = userId;
        this.tokenId = tokenId;
        this.expiresOn = expiresOn;
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public String getTokenId() {
        return tokenId;
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
