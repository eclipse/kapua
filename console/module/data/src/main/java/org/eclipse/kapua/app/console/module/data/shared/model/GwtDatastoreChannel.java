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
package org.eclipse.kapua.app.console.module.data.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtDatastoreChannel extends KapuaBaseModel {

    private static final long serialVersionUID = 1L;

    public GwtDatastoreChannel(String channel, String type) {
        set("channel", channel);
        set("type", type);
    }

    public String getChannel() {
        return get("channel");
    }

    public String getType() {
        return get("type");
    }
}
