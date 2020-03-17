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

import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;

/**
 * {@link DeviceNotifyChannel} definition.
 *
 * @since 1.0.0
 */
public interface DeviceNotifyChannel extends DeviceAppChannel {

    /**
     * Gets the request resources.
     *
     * @return The request resources.
     * @since 1.2.0
     */
    String[] getResources();

    /**
     * Sets the request resources.
     *
     * @param resources The request resources.
     * @since 1.2.0
     */
    void setResources(String[] resources);

}
