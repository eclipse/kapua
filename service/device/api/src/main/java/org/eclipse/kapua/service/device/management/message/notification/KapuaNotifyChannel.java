/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.message.notification;

import org.eclipse.kapua.service.device.management.message.KapuaAppChannel;

/**
 * Kapua notify message channel object definition.
 *
 * @since 1.0
 */
public interface KapuaNotifyChannel extends KapuaAppChannel {

    /**
     * Get the request resources
     *
     * @return
     * @since 1.2.0
     */
    String[] getResources();

    /**
     * Set the request resources
     *
     * @param resources
     * @since 1.2.0
     */
    void setResources(String[] resources);
}
