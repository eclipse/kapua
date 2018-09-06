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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

public class ManagementOperationNotificationAttributes extends KapuaUpdatableEntityAttributes {

    public static final String OPERATION_ID = "operationId";

    public static final String SENT_ON = "sentOn";

    public static final String STATUS = "status";

    public static final String PROGRESS = "progress";
}
