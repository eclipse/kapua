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
package org.eclipse.kapua.service.device.call.kura.model.asset;

/**
 * {@link KuraAssetChannel} mode definition
 *
 * @since 1.0.0
 */
public enum KuraAssetChannelMode {
    /**
     * A channel that is read only.
     * Writing to this channel will result in a error.
     */
    READ,
    /**
     * A channel that is write only.
     * Reading from this channel will result in a error.
     */
    WRITE,
    /**
     * A channel that can be read and written.
     */
    READ_WRITE
}
