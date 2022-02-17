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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.shared.model.query;

import java.io.Serializable;

public class GwtQuery implements Serializable {

    private static final long serialVersionUID = 3080860571269787362L;

    private String scopeId;
    private boolean askTotalCount = true;

    public GwtQuery() {
        super();
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public boolean getAskTotalCount() {
        return askTotalCount;
    }

    public void setAskTotalCount(boolean askTotalCount) {
        this.askTotalCount = askTotalCount;
    }

}
