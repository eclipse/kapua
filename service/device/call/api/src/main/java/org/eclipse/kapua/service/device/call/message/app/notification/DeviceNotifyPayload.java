/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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
 * {@link DeviceNotifyMessage} {@link org.eclipse.kapua.message.KapuaPayload} definition.
 *
 * @since 1.0.0
 */
public interface DeviceNotifyPayload extends DeviceAppPayload {

    /**
     * @return
     * @since 1.0.0
     */
    Long getOperationId();

    /**
     * @return
     * @since 1.0.0
     */
    String getStatus();

    /**
     * @return
     * @since 1.0.0
     */
    Integer getProgress();

    /**
     * @return
     * @since 1.2.0
     */
    String getMessage();
}
