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
package org.eclipse.kapua.app.console.shared.model.authorization;

import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;

public class GwtAccessPermission extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 1330881042880793119L;

    public String getAccessInfoId() {
        return get("accessInfoId");
    }

    public void setAccessInfoId(String accessInfoId) {
        set("accessInfoId", accessInfoId);
    }
    
    public String getPermissionDomain() {
        return get("permissionDomain");
    }
    
    public void setPermissionDomain(String permissionDomain) {
        set("permissionDomain", permissionDomain);
    }
    
    public String getPermissionAction() {
        return get("permissionAction");
    }
    
    public void setPermissionAction(String permissionAction) {
        set("permissionAction", permissionAction);
    }
    
    public String getPermissionTargetScopeId() {
        return get("permissionTargetScopeId");
    }
    
    public void setPermissionTargetScopeId(String permissionTargetScopeId) {
        set("permissionTargetScopeId", permissionTargetScopeId);
    }
    
    public String getPermissionGroupId() {
        return get("permissionGroupId");
    }
    
    public void setPermissionGroupId(String permissionGroupId) {
        set("permissionGroupId", permissionGroupId);
    }
        
}
