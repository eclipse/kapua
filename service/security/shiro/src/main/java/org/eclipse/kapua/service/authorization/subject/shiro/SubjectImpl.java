package org.eclipse.kapua.service.authorization.subject.shiro;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.subject.Subject;
import org.eclipse.kapua.service.authorization.subject.SubjectType;

/**
 * {@link Subject} implementation.
 * 
 * @since 1.0.0
 *
 */
@Embeddable
public class SubjectImpl implements Subject, Serializable {

    private static final long serialVersionUID = -7744516663474475457L;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type", updatable = false, nullable = false)
    private SubjectType subjectType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "subject_id", updatable = true, nullable = true))
    })
    private KapuaEid subjectId;

    protected SubjectImpl() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param subject
     */
    public SubjectImpl(Subject subject) {
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
    public SubjectImpl(SubjectType type, KapuaId id) {
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
        SubjectImpl other = (SubjectImpl) obj;
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
