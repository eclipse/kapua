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
package org.eclipse.kapua.app.console.module.device.shared.model;

import java.io.Serializable;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates.GwtSortAttribute;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates.GwtSortOrder;

public class GwtDeviceConnectionQueryPredicates extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 571130152596171388L;

    public GwtDeviceConnectionQueryPredicates() {
        setSortAttribute(GwtSortAttribute.CLIENT_ID.name());
        setSortOrder(GwtSortOrder.ASCENDING.name());
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("statusEnum".equals(property)) {
            return (X) (GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.valueOf(getConnectionStatus()));
        } else {
            return super.get(property);
        }
    }

    public String getClientId() {
        return (String) get("clientId");
    }

    public String getUnescapedClientId() {
        return (String) getUnescaped("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }

    public String getConnectionStatus() {
        return get("connectionStatus");
    }

    public GwtDeviceQueryPredicates.GwtDeviceConnectionStatus getConnectionStatusEnum() {
        return get("connectionStatusEnum");
    }

    public void setConnectionStatus(String deviceConnectionStatus) {
        set("connectionStatus", deviceConnectionStatus);
    }

    public String getSortOrder() {
        return (String) get("sortOrder");
    }

    public GwtSortOrder getSortOrderEnum() {
        return get("sortOrderEnum");
    }

    public void setSortOrder(String sortOrder) {
        set("sortOrder", sortOrder);
    }

    public String getSortAttribute() {
        return (String) get("sortAttribute");
    }

    public GwtSortAttribute getSortAttributeEnum() {
        return get("sortAttributeEnum");
    }

    public void setSortAttribute(String sortAttribute) {
        set("sortAttribute", sortAttribute);
    }
}
