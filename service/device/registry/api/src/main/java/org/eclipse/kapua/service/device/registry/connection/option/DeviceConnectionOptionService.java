/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option;

import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * {@link DeviceConnectionOptionService} exposes APIs to update Device connection options for a connection.
 * 
 * @since 1.0.0
 */
public interface DeviceConnectionOptionService extends KapuaEntityService<DeviceConnectionOption, DeviceConnectionOptionCreator>,
        KapuaUpdatableEntityService<DeviceConnectionOption> {
}
