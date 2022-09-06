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
package org.eclipse.kapua.service.device.management.configuration.store.settings;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreService;

/**
 * {@link DeviceConfigurationStoreEnablementPolicy} definition.
 *
 * @since 2.0.0
 */
public enum DeviceConfigurationStoreEnablementPolicy {

    /**
     * The {@link DeviceConfigurationStoreService} is enabled.
     *
     * @since 2.0.0
     */
    ENABLED,

    /**
     * The {@link DeviceConfigurationStoreService} is disabled.
     *
     * @since 2.0.0
     */
    DISABLED,

    /**
     * The {@link DeviceConfigurationStoreService} is resolved to the {@link DeviceConfigurationStoreService#getConfigValues(KapuaId)} setting.
     *
     * @since 2.0.0
     */
    INHERITED
}
