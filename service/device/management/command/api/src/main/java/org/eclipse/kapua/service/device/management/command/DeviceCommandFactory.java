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
package org.eclipse.kapua.service.device.management.command;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Device command entity service factory definition.
 *
 * @since 1.0
 */
public interface DeviceCommandFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link DeviceCommandInput}
     *
     * @return
     */
    DeviceCommandInput newCommandInput();

    /**
     * Create a new {@link DeviceCommandOutput}
     *
     * @return
     */
    DeviceCommandOutput newCommandOutput();
}
