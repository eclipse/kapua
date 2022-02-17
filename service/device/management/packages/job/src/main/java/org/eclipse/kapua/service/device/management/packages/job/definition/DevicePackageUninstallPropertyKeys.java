/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.job.definition;

import org.eclipse.kapua.service.job.step.definition.JobPropertyKey;

public class DevicePackageUninstallPropertyKeys implements JobPropertyKey {

    public static final String PACKAGE_UNINSTALL_REQUEST = "packageUninstallRequest";
    public static final String TIMEOUT = "timeout";

    private DevicePackageUninstallPropertyKeys() {
    }
}
