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
package org.eclipse.kapua.test.authorization.subject;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.subject.SubjectImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.model.subject.SubjectType;

/**
 * {@link Subject} implementation.
 * 
 * @since 1.0.0
 *
 */
@Embeddable
public class SubjectMock implements Subject, Serializable {

    public static final Subject KAPUA_SYS = new SubjectImpl(SubjectType.USER, KapuaEid.ONE);

    private static final long serialVersionUID = -7744516663474475457L;

    private SubjectType subjectType;
    private KapuaEid subjectId;

    protected SubjectMock() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param subject
     */
    public SubjectMock(Subject subject) {
        this(subject.getSubjectType(), subject.getId());
    }

    /**
     * Constructor.
     * 
     * @param type
     *            The {@link SubjectType} to set.
     * @param id
     *            The {@link Subject} id to set.
     * 
     * @since 1.0.0
     */
    public SubjectMock(SubjectType type, KapuaId id) {
        this();
        setType(type);
        setId(id);
    }

    @Override
    public SubjectType getSubjectType() {
        return subjectType;
    }

    @Override
    public void setType(SubjectType type) {
        this.subjectType = type;
    }

    @Override
    public KapuaId getId() {
        return subjectId;
    }

    @Override
    public void setId(KapuaId id) {
        if (id != null) {
            this.subjectId = new KapuaEid(id);
        } else {
            this.subjectId = null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subjectId == null) ? 0 : subjectId.hashCode());
        result = prime * result + ((subjectType == null) ? 0 : subjectType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubjectMock other = (SubjectMock) obj;
        if (subjectId == null) {
            if (other.subjectId != null)
                return false;
        } else if (!subjectId.equals(other.subjectId))
            return false;
        if (subjectType != other.subjectType)
            return false;
        return true;
    }
}
