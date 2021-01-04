/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.snapshot.internal.exception;

import org.eclipse.kapua.service.device.management.exception.DeviceManagementErrorCodes;

public enum SnapshotManagementResponseErrorCodes implements DeviceManagementErrorCodes {

    /**
     * The device has returned an error when getting snapshot
     */
    SNAPSHOT_GET_ERROR,

    /**
     * The device has returned an error when rollback shapshot
     */
    SNAPSHOT_ROLLBACK_ERROR,
}
