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
package org.eclipse.kapua.service.device.management.job;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link JobDeviceManagementOperation} xml factory class
 *
 * @since 1.1.0
 */
@XmlRegistry
public class JobDeviceManagementOperationXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobDeviceManagementOperationFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobDeviceManagementOperationFactory.class);

    public JobDeviceManagementOperation newJobDeviceManagementOperation() {
        return JOB_STEP_FACTORY.newEntity(null);
    }

    public JobDeviceManagementOperationCreator newJobDeviceManagementOperationCreator() {
        return JOB_STEP_FACTORY.newCreator(null);
    }

    public JobDeviceManagementOperationListResult newJobDeviceManagementOperationListResult() {
        return JOB_STEP_FACTORY.newListResult();
    }

    public JobDeviceManagementOperationQuery newQuery() {
        return JOB_STEP_FACTORY.newQuery(null);
    }
}
