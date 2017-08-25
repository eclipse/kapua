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
package org.eclipse.kapua.app.console.module.device.server;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionOption;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionOptionService;
import org.eclipse.kapua.app.console.module.device.shared.util.KapuaGwtDeviceModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;

public class GwtDeviceConnectionOptionServiceImpl extends KapuaRemoteServiceServlet implements GwtDeviceConnectionOptionService {

    private static final long serialVersionUID = 7323313459749361320L;

    @Override
    public GwtDeviceConnectionOption update(GwtXSRFToken gwtXsrfToken, GwtDeviceConnectionOption gwtDeviceConnectionOption) throws GwtKapuaException {
        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do update
        try {
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionOption.getScopeId());
            KapuaId deviceConnectionOptionId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionOption.getId());
            // Update
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceConnectionOptionService deviceConnectionOptionService = locator.getService(DeviceConnectionOptionService.class);

            DeviceConnectionOption deviceConnectionOption = deviceConnectionOptionService.find(scopeId, deviceConnectionOptionId);

            deviceConnectionOption.setAllowUserChange(gwtDeviceConnectionOption.getAllowUserChange());
            deviceConnectionOption.setReservedUserId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionOption.getReservedUserId()));
            if (gwtDeviceConnectionOption.getConnectionUserCouplingModeEnum() != null) {
                deviceConnectionOption.setUserCouplingMode(ConnectionUserCouplingMode.valueOf(gwtDeviceConnectionOption.getConnectionUserCouplingModeEnum().name()));
            }

            return KapuaGwtDeviceModelConverter.convertDeviceConnectionOption(deviceConnectionOptionService.update(deviceConnectionOption));

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtDeviceConnectionOption;
    }

}
