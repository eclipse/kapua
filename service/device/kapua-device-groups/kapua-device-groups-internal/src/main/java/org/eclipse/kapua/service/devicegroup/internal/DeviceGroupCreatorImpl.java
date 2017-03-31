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
package org.eclipse.kapua.service.devicegroup.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.devicegroup.DeviceGroup;
import org.eclipse.kapua.service.devicegroup.DeviceGroupCreator;

public class DeviceGroupCreatorImpl extends AbstractKapuaNamedEntityCreator<DeviceGroup>
        implements DeviceGroupCreator {

    /**
     * 
     */
    private static final long serialVersionUID = -6520976352389387274L;
    private KapuaId devId;
    private KapuaId groupId;

    protected DeviceGroupCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId, name);
    }

    @Override
    public KapuaId getDevId() {
        return devId;
    }

    @Override
    public void setDevId(KapuaId devId) {
        this.devId = devId;
    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = groupId;
    }

}
