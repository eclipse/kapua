/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.device.shared.model.management.registry.GwtDeviceManagementOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.management.registry.GwtDeviceManagementOperationQuery;

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
