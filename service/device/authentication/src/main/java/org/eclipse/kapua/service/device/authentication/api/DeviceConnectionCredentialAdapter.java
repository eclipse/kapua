/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.authentication.api;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.service.authentication.LoginCredentials;

/**
 * {@link DeviceConnectionCredentialAdapter} definition.
 *
 * @since 2.0.0
 */
public interface DeviceConnectionCredentialAdapter {

    /**
     * Maps an {@link AuthRequest} to a {@link LoginCredentials} implementation
     *
     * @param authRequest The {@link AuthRequest} to map.
     * @return The corresponding {@link LoginCredentials}
     * @throws KapuaException if there is any error while doing the mapping
     * @since 2.0.0
     */
    LoginCredentials mapToCredential(AuthRequest authRequest) throws KapuaException;
}
