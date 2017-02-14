package org.eclipse.kapua.app.console.shared.model.authorization;

import org.eclipse.kapua.app.console.shared.model.GwtEntityCreator;

public class GwtRolePermissionCreator extends GwtEntityCreator{

    private static final long serialVersionUID = -1429654300918034614L;
    
    String scopeId;
    String roleId;
    
    public String getScopeId() {
        return scopeId;
    }
    
    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
}
