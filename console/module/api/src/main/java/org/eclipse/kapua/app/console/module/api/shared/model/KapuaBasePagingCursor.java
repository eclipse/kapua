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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModel;

public class KapuaBasePagingCursor extends BaseModel implements Serializable {

    private static final long serialVersionUID = 5814738657797246416L;

    public KapuaBasePagingCursor() {
    }

    public Object getKeyOffset() {
        return get("keyOffset");
    }

    public void setKeyOffset(Object keyOffset) {
        set("keyOffset", keyOffset);
    }
}
