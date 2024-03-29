/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

public class ManagementOperationNotificationAttributes extends KapuaUpdatableEntityAttributes {

    public static final String OPERATION_ID = "operationId";

    public static final String SENT_ON = "sentOn";

    public static final String STATUS = "status";

    public static final String PROGRESS = "progress";

    public static final String CHECKPOINT = "checkpoint";

    public static final String RESOURCE = "resource";
}
