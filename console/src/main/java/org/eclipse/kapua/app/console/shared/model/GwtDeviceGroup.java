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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtDeviceGroup extends GwtUpdatableEntityModel implements IsSerializable {

    private static final long serialVersionUID = 5838361931245750282L;

    public GwtDeviceGroup() {
        super();
    }

    public String getDevId() {
        return (String) get("devId");
    }

    public void setDevId(String devId) {
        set("devId", devId);
    }

    public String getGroupId() {
        return (String) get("groupId");
    }

    public void setGroupId(String groupId) {
        set("groupId", groupId);
    }

    public void setGroupName(String name) {
       set ("groupName", name);  
    }
    
    public String getGroupName(){
        return (String) get("groupName");
    }

}
