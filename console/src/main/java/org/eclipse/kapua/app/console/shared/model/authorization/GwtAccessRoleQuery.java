package org.eclipse.kapua.app.console.shared.model.authorization;

import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;

public class GwtAccessRoleQuery extends GwtQuery{

    /**
     * 
     */
    private static final long serialVersionUID = 1007100209409375314L;
    private String accessInfoId;
    private String roleId;
    public String getAccessInfoId() {
        return accessInfoId;
    }
    public void setAccessInfoId(String accessInfoId) {
        this.accessInfoId = accessInfoId;
    }
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    
            
}
