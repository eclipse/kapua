/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.packages.dialog;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityLogDialog;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry.GwtDeviceManagementOperation;

public class DeviceManagementOperationLog extends EntityLogDialog {

    private static final ConsoleDeviceMessages MSGS_DEVICE = GWT.create(ConsoleDeviceMessages.class);

    public DeviceManagementOperationLog(GwtDeviceManagementOperation deviceManagementOperation) {
        super(MSGS_DEVICE.deviceInstallTabHistoryDialogLogHeader(), deviceManagementOperation.getUnescapedLog());
    }
}
