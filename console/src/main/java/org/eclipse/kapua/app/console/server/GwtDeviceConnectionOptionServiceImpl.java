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
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnectionOption;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceConnectionOptionService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
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
            KapuaId scopeId = GwtKapuaModelConverter.convert(gwtDeviceConnectionOption.getScopeId());
            KapuaId deviceConnectionOptionId = GwtKapuaModelConverter.convert(gwtDeviceConnectionOption.getId());
            // Update
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceConnectionOptionService deviceConnectionOptionService = locator.getService(DeviceConnectionOptionService.class);

            DeviceConnectionOption deviceConnectionOption = deviceConnectionOptionService.find(scopeId, deviceConnectionOptionId);

            deviceConnectionOption.setAllowUserChange(gwtDeviceConnectionOption.getAllowUserChange());
            deviceConnectionOption.setReservedUserId(GwtKapuaModelConverter.convert(gwtDeviceConnectionOption.getReservedUserId()));
            if (gwtDeviceConnectionOption.getConnectionUserCouplingModeEnum() != null) {
                deviceConnectionOption.setUserCouplingMode(ConnectionUserCouplingMode.valueOf(gwtDeviceConnectionOption.getConnectionUserCouplingModeEnum().name()));
            }

            return KapuaGwtModelConverter.convert(deviceConnectionOptionService.update(deviceConnectionOption));

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtDeviceConnectionOption;
    }

}
