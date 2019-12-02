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
package org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

public class GwtDeviceManagementOperationQuery extends GwtQuery {

    private static final long serialVersionUID = 7269983474348658584L;

    private String deviceId;
    private String appId;

    public GwtDeviceManagementOperationQuery() {
        super();

    }

    public GwtDeviceManagementOperationQuery(String scopeId) {
        this();
        setScopeId(scopeId);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
