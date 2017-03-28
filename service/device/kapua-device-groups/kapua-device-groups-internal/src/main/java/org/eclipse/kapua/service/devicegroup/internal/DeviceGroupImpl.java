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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.devicegroup.DeviceGroup;

@Entity(name = "DeviceGroup")
@Table(name = "dvc_dev_group")
public class DeviceGroupImpl extends AbstractKapuaUpdatableEntity implements DeviceGroup {

    private static final long serialVersionUID = -3316208063245977762L;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "eid", column = @Column(name = "dev_id")) })
    private KapuaEid devId;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "eid", column = @Column(name = "group_id")) })
    private KapuaEid groupId;

    public DeviceGroupImpl() {
        super();
    }

    public DeviceGroupImpl(KapuaId scopeId, String name) {
        super(scopeId);
    }

    @Override
    public KapuaId getDevId() {
        return devId;
    }

    @Override
    public void setDevId(KapuaId devId) {
        this.devId = (KapuaEid) devId;

    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = (KapuaEid) groupId;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
