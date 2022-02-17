/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cucumber;

import java.math.BigInteger;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;

public class CucGroup {

    private String name;
    private Integer scope;
    private KapuaId scopeId;

    public void doParse() {
        if (this.scope != null) {
            this.scopeId = new KapuaEid(BigInteger.valueOf(scope.longValue()));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }
}
