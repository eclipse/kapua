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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link DeviceFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface DeviceFactory extends KapuaEntityFactory<Device, DeviceCreator, DeviceQuery, DeviceListResult> {

    /**
     * Instantiates a new {@link DeviceCreator}
     *
     * @param scopeId  The scope {@link KapuaId} to set into the {@link DeviceCreator}
     * @param clientId The client id to set into the {@link DeviceCreator}
     * @return The newly instantiated {@link DeviceCreator}.
     * @since 1.0.0
     */
    DeviceCreator newCreator(KapuaId scopeId, String clientId);

}
