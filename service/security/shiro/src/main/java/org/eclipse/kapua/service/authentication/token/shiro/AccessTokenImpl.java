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
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.generator.id.IdGeneratorService;

/**
 * Access token entity implementation.
 * 
 * @since 1.0
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "AccessToken")
@Table(name = "atht_access_token")
public class AccessTokenImpl extends AbstractKapuaUpdatableEntity implements AccessToken {

    private static final long serialVersionUID = -6003387376828196787L;

    @XmlElement(name = "userId")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id", updatable = false, nullable = false))
    })
    private KapuaEid userId;

    @XmlElement(name = "tokenId")
    @Basic
    @Column(name = "token_id", updatable = false, nullable = false)
    private String tokenId;

    @XmlElement(name = "expiresOn")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_on", nullable = false)
    private Date expiresOn;

    /**
     * Constructor
     */
    public AccessTokenImpl() {
        super();
    }

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
        this.userId = new KapuaEid(userId.getId());
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

    @Override
    protected void prePersistsAction()
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        IdGeneratorService idGenerator = locator.getService(IdGeneratorService.class);

        this.id = new KapuaEid(idGenerator.generate().getId());
        this.createdBy = userId;
        this.createdOn = new Date();
    }
}
