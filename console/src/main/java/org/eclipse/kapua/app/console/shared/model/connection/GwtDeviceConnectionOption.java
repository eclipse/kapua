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
package org.eclipse.kapua.app.console.shared.model.connection;

import java.io.Serializable;

import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection.GwtConnectionUserCouplingMode;

public class GwtDeviceConnectionOption extends GwtUpdatableEntityModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5059430797167078209L;

    public GwtDeviceConnectionOption() {
    }

    public GwtDeviceConnectionOption(GwtDeviceConnection gwtDeviceConnection) {
        setId(gwtDeviceConnection.getId());
        setScopeId(gwtDeviceConnection.getScopeId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(String property) {
        if ("connectionUserCouplingModeEnum".equals(property)) {
            return (X) GwtConnectionUserCouplingMode.getEnumFromLabel(getConnectionUserCouplingMode());
        } else {
            return super.get(property);
        }
    }

    public String getConnectionUserCouplingMode() {
        return get("connectionUserCouplingMode");
    }

    public GwtConnectionUserCouplingMode getConnectionUserCouplingModeEnum() {
        return get("connectionUserCouplingModeEnum");
    }

    public void setConnectionUserCouplingMode(String connectionUserCouplingMode) {
        set("connectionUserCouplingMode", connectionUserCouplingMode);
    }

    public String getUserId() {
        return get("userId");
    }

    public void setUserId(String userId) {
        set("userId", userId);
    }

    public String getReservedUserId() {
        return get("reservedUserId");
    }

    public void setReservedUserId(String userId) {
        set("reservedUserId", userId);
    }

    public Boolean getAllowUserChange() {
        return get("allowUserChange");
    }

    public void setAllowUserChange(Boolean allowUserChange) {
        set("allowUserChange", allowUserChange);
    }

}
