/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device.deviceGroup;

import org.eclipse.kapua.app.console.shared.model.GwtEntityCreator;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtDeviceGroupCreator extends GwtEntityCreator implements IsSerializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4132001506604910426L;
    private String scopeId;
    private String devId;
    private String groupId;

    public GwtDeviceGroupCreator() {

    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
