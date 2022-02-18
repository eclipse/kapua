/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaChannel;

/**
 * {@link KapuaLifecycleChannel} definition
 *
 * @since 1.1.0
 */
public interface KapuaLifecycleChannel extends KapuaChannel {

    /**
     * Gets client id
     *
     * @return The client id
     * @since 1.1.0
     */
    String getClientId();

    /**
     * Sets client id
     *
     * @param clientId The client id
     * @since 1.1.0
     */
    void setClientId(String clientId);
}
