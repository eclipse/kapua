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
package org.eclipse.kapua.service.device.steps;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;

import java.math.BigInteger;

/**
 * Data object used for PermissionData configuration.
 */
public class PermissionData {

    private Domain domain;

    private Actions action;

    private KapuaEid targetScopeId;

    public PermissionData(Domain domain, Actions action, KapuaEid targetScopeId) {
        this.domain = domain;
        this.action = action;
        this.targetScopeId = targetScopeId;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
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
