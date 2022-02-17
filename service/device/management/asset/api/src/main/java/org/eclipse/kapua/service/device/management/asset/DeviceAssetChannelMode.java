/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.asset;

/**
 * {@link DeviceAssetChannel} modes definition.
 *
 * @since 1.0.0
 */
public enum DeviceAssetChannelMode {

    /**
     * Write only.
     *
     * @since 1.0.0
     */
    WRITE,

    /**
     * Read only.
     *
     * @since 1.0.0
     */
    READ,

    /**
     * Read and write.
     *
     * @since 1.0.0
     */
    READ_WRITE
}
