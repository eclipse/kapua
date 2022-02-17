/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;

/**
 * @since 1.4.0
 */
public enum DeviceManagementOperationStatus {

    /**
     * @since 1.4.0
     */
    RUNNING,

    /**
     * @since 1.4.0
     */
    COMPLETED,

    /**
     * @since 1.4.0
     */
    FAILED,

    /*
     * @since 1.4.0
     */
    STALE;

    public static DeviceManagementOperationStatus readFrom(NotifyStatus notifyStatus) {
        switch (notifyStatus) {
            case RUNNING:
                return DeviceManagementOperationStatus.RUNNING;
            case COMPLETED:
                return DeviceManagementOperationStatus.COMPLETED;
            case FAILED:
                return DeviceManagementOperationStatus.FAILED;
            case STALE:
                return DeviceManagementOperationStatus.STALE;
            default:
                return null;
        }
    }
}