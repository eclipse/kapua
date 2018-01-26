/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtAccessRole extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 1330881042880793119L;

    public String getAccessInfoId() {
        return get("accessInfoId");
    }

    public void setAccessInfoId(String accessInfoId) {
        set("accessInfoId", accessInfoId);
    }

    public String getRoleId() {
        return get("roleId");
    }

    public void setRoleId(String roleId) {
        set("roleId", roleId);
    }

    public String getRoleName() {
        return get("roleName");
    }

    public void setRoleName(String roleName) {
        set("roleName", roleName);
    }

}
