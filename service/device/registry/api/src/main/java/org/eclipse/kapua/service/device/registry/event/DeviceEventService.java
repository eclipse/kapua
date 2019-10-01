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
package org.eclipse.kapua.service.device.registry.event;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * {@link DeviceEventService} definition.
 *
 * @since 1.0.0
 */
public interface DeviceEventService extends KapuaEntityService<DeviceEvent, DeviceEventCreator> {

    /**
     * Returns the {@link DeviceEventListResult} with elements matching the provided query.
     *
     * @param query The {@link DeviceEventQuery} used to filter results.
     * @return The {@link DeviceEventListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    DeviceEventListResult query(KapuaQuery<DeviceEvent> query) throws KapuaException;

}
