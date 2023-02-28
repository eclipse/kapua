/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaTransactedRepository;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationTransactedRepository;

import java.util.function.Supplier;

public class ManagementOperationNotificationImplJpaTransactedRepository
        extends KapuaEntityJpaTransactedRepository<ManagementOperationNotification, ManagementOperationNotificationImpl, ManagementOperationNotificationListResult>
        implements ManagementOperationNotificationTransactedRepository {
    public ManagementOperationNotificationImplJpaTransactedRepository(Supplier<ManagementOperationNotificationListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(ManagementOperationNotificationImpl.class, listSupplier, entityManagerSession);
    }
}
