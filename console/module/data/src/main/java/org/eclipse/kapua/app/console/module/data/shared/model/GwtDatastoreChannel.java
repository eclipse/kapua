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
