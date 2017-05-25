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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import java.math.BigInteger;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Actions;

public class CucRolePermission {

    private Integer scopeId;
    private Integer roleId;
    private String actionName;
    private KapuaId scope;
    private KapuaId role;
    private Actions action;

    public void doParse() {
        if (this.scopeId != null) {
            this.scope = new KapuaEid(BigInteger.valueOf(scopeId));
        }
        if (this.roleId != null) {
            this.role = new KapuaEid(BigInteger.valueOf(roleId));
        }
        if (this.actionName != null) {
            switch (actionName.trim().toLowerCase()) {
            case "read":
                this.action = Actions.read;
                break;
            case "write":
                this.action = Actions.write;
                break;
            case "delete":
                this.action = Actions.delete;
                break;
            case "connect":
                this.action = Actions.connect;
                break;
            case "execute":
                this.action = Actions.execute;
                break;
            }
        }
    }

    public void setScopeId(KapuaId id) {
        this.scope = id;
    }

    public KapuaId getScopeId() {
        return this.scope;
    }

    public void setRoleId(KapuaId id) {
        this.role = id;
    }

    public KapuaId getRoleId() {
        return this.role;
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }
}
