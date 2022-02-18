/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;

import java.math.BigInteger;

/**
 * Data object used for PermissionData configuration.
 */
public class AclPermission {

    private Domain domain;

    private Actions action;

    private KapuaEid targetScopeId;

    public AclPermission(Domain domain, Actions action, KapuaEid targetScopeId) {
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
