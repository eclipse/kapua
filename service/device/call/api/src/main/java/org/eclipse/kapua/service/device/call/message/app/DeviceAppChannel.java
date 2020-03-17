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
package org.eclipse.kapua.service.device.call.message.app;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;

/**
 * {@link DeviceAppChannel} definition.
 *
 * @since 1.0.0
 */
public interface DeviceAppChannel extends DeviceChannel {

    /**
     * Gets the application identifier.
     *
     * @return The application identifier.
     * @since 1.0.0
     */
    String getAppId();

    /**
     * Sets the application identifier
     *
     * @param appId The application identifier.
     * @since 1.0.0
     */
    void setAppId(String appId);

}
