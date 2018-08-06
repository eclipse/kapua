/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

public interface DeviceManagementOperationPredicates extends KapuaUpdatableEntityPredicates {

    String STARTED_ON = "startedOn";

    String ENDED_ON = "endedOn";

    String DEVICE_ID = "deviceId";

    String APP_ID = "appId";

    String ACTION = "action";

    String RESOURCE = "resource";

    String STATUS = "status";
}
