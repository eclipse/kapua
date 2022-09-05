/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.app.config;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * @since 2.0.0
 */
public interface ConfigurableDeviceAppManagementService<C extends DeviceAppManagementConfiguration> {

    C getApplicationConfiguration(KapuaId scopeId, KapuaId deviceId) throws KapuaException;

    void setApplicationConfiguration(KapuaId scopeId, KapuaId deviceId, C applicationConfiguration) throws KapuaException;
}