/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
