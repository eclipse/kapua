/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperationQuery;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deviceManagementOperation")
public interface GwtDeviceManagementOperationService extends RemoteService {

    /**
     * Returns the list of all {@link org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation}s matching the query.
     *
     * @param gwtDeviceManagementOperationQuery
     * @return
     * @throws GwtKapuaException
     */
    PagingLoadResult<GwtDeviceManagementOperation> query(PagingLoadConfig loadConfig, GwtDeviceManagementOperationQuery gwtDeviceManagementOperationQuery)
            throws GwtKapuaException;
}
