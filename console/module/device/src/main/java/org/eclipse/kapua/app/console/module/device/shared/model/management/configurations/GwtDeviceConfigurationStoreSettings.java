/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.configurations;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;

public class GwtDeviceConfigurationStoreSettings extends KapuaBaseModel implements Serializable {

    public enum GwtDeviceConfigurationStoreEnablementPolicy {
        INHERITED,

        ENABLED,

        DISABLED
    }

    @Override
    public <X> X get(String property) {
        if ("storeEnablementPolicyEnum".equals(property)) {
            return (X) GwtDeviceConfigurationStoreEnablementPolicy.valueOf(getStoreEnablementPolicy());
        }

        return super.get(property);
    }

    public String getStoreEnablementPolicy() {
        return get("storeEnablementPolicy");
    }

    public GwtDeviceConfigurationStoreEnablementPolicy getStoreEnablementPolicyEnum() {
        return get("storeEnablementPolicyEnum");
    }

    public void setStoreEnablementPolicy(String storeEnablementPolicy) {
        set("storeEnablementPolicy", storeEnablementPolicy);
    }

    public void setStoreEnablementPolicy(GwtDeviceConfigurationStoreEnablementPolicy storeEnablementPolicy) {
        set("storeEnablementPolicy", storeEnablementPolicy.name());
    }
}
