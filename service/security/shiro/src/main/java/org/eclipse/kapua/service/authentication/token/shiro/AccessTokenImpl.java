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
package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.subject.SubjectImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.service.authentication.token.AccessToken;

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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "subjectType", column = @Column(name = "subject_type", nullable = false, updatable = true)),
            @AttributeOverride(name = "subjectId.eid", column = @Column(name = "subject_id", nullable = false, updatable = true))
    })
    private SubjectImpl subject;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "credential_id", nullable = false))
    })
    private KapuaEid credentialId;

    @Basic
    @Column(name = "token_id", updatable = false, nullable = false)
    private String tokenId;

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
     * Constructor.
     * 
     * @param scopeId
     * @param subject
     * @param credentialId
     * @param tokenId
     * @param expiresOn
     * 
     * @since 1.0.0
     */
    public AccessTokenImpl(KapuaId scopeId, Subject subject, KapuaId credentialId, String tokenId, Date expiresOn) {
        super(scopeId);
        setSubject(subject);
        setCredentialId(credentialId);
        setTokenId(tokenId);
        setExpiresOn(expiresOn);
    }

    @Override
    public Subject getSubject() {
        return subject;
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject != null ? (subject instanceof SubjectImpl ? (SubjectImpl) subject : new SubjectImpl(subject)) : null;
    }

    @Override
    public KapuaId getCredentialId() {
        return credentialId;
    }

    @Override
    public void setCredentialId(KapuaId credentialId) {
        this.credentialId = credentialId != null ? (credentialId instanceof KapuaEid ? (KapuaEid) credentialId : new KapuaEid(credentialId)) : null;
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

    /**
     * Before update action we need to correctly set the {@link #modifiedOn} and {@link #modifiedBy} fields.
     *
     * @throws KapuaException
     */
    @PreUpdate
    protected void preUpdateAction() {
        Subject currentSubject = KapuaSecurityUtils.getSession().getSubject();
        if (currentSubject != null && currentSubject.getId() != null) {
            super.preUpdateAction();
        } else {
            setModifiedBy(SubjectImpl.KAPUA_SYS);
            setModifiedOn(new Date());
        }
    }
}
