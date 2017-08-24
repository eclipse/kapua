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
