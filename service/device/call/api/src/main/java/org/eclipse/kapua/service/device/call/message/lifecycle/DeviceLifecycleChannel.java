/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.lifecycle;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;

/**
 * {@link DeviceLifecycleChannel} definition.
 *
 * @since 1.0.0
 */
public interface DeviceLifecycleChannel extends DeviceChannel {

    /**
     * Gets the lifecycle phase.
     * <p>
     * Lifecycle has many phases that determines different aspects of the application.
     *
     * @return The phase.
     * @since 1.2.0
     */
    String getPhase();
}
