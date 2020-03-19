/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.app.notification;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppPayload;

/**
 * {@link DeviceNotifyPayload} definition.
 *
 * @since 1.0.0
 */
public interface DeviceNotifyPayload extends DeviceAppPayload {

    /**
     * Gets the device management operation identifier.
     *
     * @return The device management operation identifier.
     * @since 1.0.0
     */
    Long getOperationId();

    /**
     * Sets the device management operation identifier.
     *
     * @return The device management operation identifier.
     * @since 1.0.0
     */
    String getStatus();

    /**
     * Gets the progress of the processing.
     *
     * @return The progress of the processing.
     * @since 1.0.0
     */
    Integer getProgress();

    /**
     * Sets the progress of the processing.
     *
     * @return The progress of the processing.
     * @since 1.2.0
     */
    String getMessage();
}
