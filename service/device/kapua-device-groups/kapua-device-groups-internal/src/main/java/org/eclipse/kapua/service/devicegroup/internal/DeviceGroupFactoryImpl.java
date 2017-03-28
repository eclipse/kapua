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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.devicegroup.DevGroupListResult;
import org.eclipse.kapua.service.devicegroup.DevGroupQuery;
import org.eclipse.kapua.service.devicegroup.DeviceGroup;
import org.eclipse.kapua.service.devicegroup.DeviceGroupCreator;
import org.eclipse.kapua.service.devicegroup.DeviceGroupFactory;

@KapuaProvider
public class DeviceGroupFactoryImpl implements DeviceGroupFactory {

    @Override
    public DeviceGroupCreator newCreator(KapuaId scopedId, String name) {
        return new DeviceGroupCreatorImpl(scopedId, name);
    }

    @Override
    public DeviceGroup newDevGroup() {
        return new DeviceGroupImpl();
    }

    @Override
    public DevGroupQuery newQuery(KapuaId scopedId) {
        return new DevGroupQueryImpl(scopedId);
    }

    @Override
    public DevGroupListResult newDevGroupListResult() {
        return new DevGroupResultImpl();
    }

}
