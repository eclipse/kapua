package org.eclipse.kapua.service.authorization.access.shiro;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;

/**
 * {@link AccessRole} implementation
 * 
 * @since 1.0
 */
@Entity(name = "AccessRole")
@Table(name = "athz_access_role")
public class AccessRoleImpl extends AbstractKapuaEntity implements AccessRole {

    private static final long serialVersionUID = 8400951097610833058L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "access_info_id"))
    })
    private KapuaEid accessId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleImpl role;

    protected AccessRoleImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AccessRoleImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor
     * 
     * @param accessRole
     * @throws KapuaException
     */
    public AccessRoleImpl(AccessRole accessRole) throws KapuaException {
        super((AbstractKapuaEntity) accessRole);

        setAccessId(accessRole.getAccessId());
        setRole(accessRole.getRole());
    }

    @Override
    public void setAccessId(KapuaId accessId) {
        if (accessId != null) {
            this.accessId = new KapuaEid(accessId);
        } else {
            this.accessId = null;
        }
    }

    @Override
    public KapuaId getAccessId() {
        return accessId;
    }

    @Override
    public void setRole(Role role) throws KapuaException {
        this.role = new RoleImpl(role);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RoleImpl getRole() {
        return role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessId == null) ? 0 : accessId.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
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
        AccessRoleImpl other = (AccessRoleImpl) obj;
        if (accessId == null) {
            if (other.accessId != null)
                return false;
        } else if (!accessId.equals(other.accessId))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }
}
