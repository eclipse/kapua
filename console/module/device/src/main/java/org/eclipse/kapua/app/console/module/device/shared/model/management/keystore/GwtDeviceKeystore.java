/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.keystore;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtDeviceKeystore extends KapuaBaseModel {

    public String getId() {
        return get("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getKeystoreType() {
        return get("keystoreType");
    }

    public void setKeystoreType(String keystoreType) {
        set("keystoreType", keystoreType);
    }

    public Integer getSize() {
        return get("size");
    }

    public void setSize(Integer size) {
        set("size", size);
    }
}
