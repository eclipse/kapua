/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.server;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.common.primitives.Ints;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperationQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementOperationService;
import org.eclipse.kapua.app.console.module.device.shared.util.GwtKapuaDeviceModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.util.KapuaGwtDeviceModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;

import java.util.ArrayList;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class GwtDeviceManagementOperationServiceImpl extends KapuaRemoteServiceServlet implements GwtDeviceManagementOperationService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);

    @Override
    public PagingLoadResult<GwtDeviceManagementOperation> query(PagingLoadConfig loadConfig, GwtDeviceManagementOperationQuery gwtQuery) throws GwtKapuaException {

        int totalLength = 0;
        List<GwtDeviceManagementOperation> gwtDeviceManagementOperations = new ArrayList<GwtDeviceManagementOperation>();
        try {
            DeviceManagementOperationQuery query = GwtKapuaDeviceModelConverter.convertDeviceManagementOperationQuery(loadConfig, gwtQuery);

            DeviceManagementOperationListResult managementOperations = DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.query(query);
            totalLength = Ints.checkedCast(DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.count(query));

            for (DeviceManagementOperation dmo : managementOperations.getItems()) {
                gwtDeviceManagementOperations.add(KapuaGwtDeviceModelConverter.convertManagementOperation(dmo));
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtDeviceManagementOperation>(gwtDeviceManagementOperations, loadConfig.getOffset(), totalLength);
    }
}
