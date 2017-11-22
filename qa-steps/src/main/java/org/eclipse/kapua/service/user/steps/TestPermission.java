/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import java.math.BigInteger;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.authorization.permission.Actions;

/**
 * Data object used in Gherkin to transfer Permission data.
 */
public class TestPermission {

    private String domain;

    private Actions action;

    private KapuaEid targetScopeId;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public KapuaEid getTargetScopeId() {
        return targetScopeId;
    }

    public void setTargetScopeId(KapuaEid targetScopeId) {
        this.targetScopeId = targetScopeId;
    }

    public void setTargetScopeId(BigInteger targetScopeId) {
        this.targetScopeId = new KapuaEid(targetScopeId);
    }
}
