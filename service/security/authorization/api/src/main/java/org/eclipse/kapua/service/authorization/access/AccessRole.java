package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;

/**
 * Access role entity.<br>
 * Describes a role associated to the access info.
 * 
 * @since 1.0
 *
 */
public interface AccessRole extends KapuaEntity {

    public static final String TYPE = "accessRole";

    public default String getType() {
        return TYPE;
    }

    /**
     * Set the access identifier
     * 
     * @param accessId
     */
    public void setAccessId(KapuaId accessId);

    /**
     * Get the access identifier
     * 
     * @return
     */
    public KapuaId getAccessId();

    /**
     * Set the role
     * 
     * @param role
     * @throws KapuaException
     */
    public void setRole(Role role) throws KapuaException;

    /**
     * Get the role
     * 
     * @return
     */
    public <R extends Role> R getRole();
}
