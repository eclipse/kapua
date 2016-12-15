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
package org.eclipse.kapua.service.authorization.access.shiro;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

/**
 * {@link AccessInfo} implementation.
 * 
 * @since 1.0.0
 */
@Entity(name = "AccessInfo")
@Table(name = "athz_access_info")
public class AccessInfoImpl extends AbstractKapuaUpdatableEntity implements AccessInfo {

    private static final long serialVersionUID = -3760818776351242930L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id", nullable = false, updatable = false))
    })
    private KapuaEid userId;

    /**
     * Empty constructor required by JPA.
     * 
     * @since 1.0.0
     */
    protected AccessInfoImpl() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param scopeId
     *            The scope id to set for this {@link AccessInfo}.
     * 
     * @since 1.0.0
     */
    public AccessInfoImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.<br>
     * Creates a clone of the given {@link AccessInfo}.
     * 
     * @param accessInfo
     *            The {@link AccessInfo} object to clone into this {@link AccessInfo}.
     * @throws KapuaException
     *             If the given {@link AccessInfo} is incompatible with the implementation-specific type.
     * @since 1.0.0
     */
    public AccessInfoImpl(AccessInfo accessInfo) throws KapuaException {
        super((AbstractKapuaUpdatableEntity) accessInfo);

        setUserId(accessInfo.getUserId());
    }

    @Override
    public void setUserId(KapuaId userId) {
        if (userId != null) {
            this.userId = new KapuaEid(userId.getId());
        } else {
            this.userId = null;
        }
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }
}
