/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle.internal.exception;

import org.eclipse.kapua.service.device.management.exception.DeviceManagementErrorCodes;

/**
 * {@link DeviceManagementErrorCodes} for {@link InventoryManagementResponseException}.
 *
 * @since 1.5.0
 */
public enum InventoryManagementResponseErrorCodes implements DeviceManagementErrorCodes {

    /**
     * @see @{@link DeviceInventoryGetManagementException}
     * @since 1.5.0
     */
    BUNDLE_GET_ERROR,
}
