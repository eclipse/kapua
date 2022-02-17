/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.client.device.packages.dialog;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityLogDialog;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.management.registry.GwtDeviceManagementOperation;

public class DeviceManagementOperationLog extends EntityLogDialog {

    private static final ConsoleDeviceMessages MSGS_DEVICE = GWT.create(ConsoleDeviceMessages.class);

    public DeviceManagementOperationLog(GwtDeviceManagementOperation deviceManagementOperation) {
        super(MSGS_DEVICE.deviceInstallTabHistoryDialogLogHeader(), deviceManagementOperation.getUnescapedLog());
    }
}
