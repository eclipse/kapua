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
package org.eclipse.kapua.qa.common.cucumber;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import java.math.BigInteger;

/**
 * Data object used in Gherkin to transfer Permission data.
 */
public class CucPermission {

    private String domain;

    private Actions action;

    private Integer targetScope;
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
        if (targetScope != null) {
            return targetScopeId = new KapuaEid(BigInteger.valueOf(targetScope));
        }
        return targetScopeId;
    }

    public void setTargetScopeId(KapuaEid targetScopeId) {
        this.targetScopeId = targetScopeId;
    }

    public void setTargetScopeId(BigInteger targetScopeId) {
        this.targetScopeId = new KapuaEid(targetScopeId);
    }
}
