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

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.subject.SubjectImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
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
    private SubjectImpl subject;

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

        setSubject(accessInfo.getSubject());
    }

    @Override
    public Subject getSubject() {
        return subject;
    }

    @Override
    public void setSubject(Subject subject) {
        if (subject != null) {
            this.subject = new SubjectImpl(subject);
        } else {
            this.subject = null;
        }
    }

}
