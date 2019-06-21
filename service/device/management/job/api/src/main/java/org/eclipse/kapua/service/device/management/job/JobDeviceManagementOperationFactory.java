/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link JobDeviceManagementOperationFactory} {@link org.eclipse.kapua.model.KapuaObjectFactory} definition.
 *
 * @since 1.1.0
 */
public interface JobDeviceManagementOperationFactory extends KapuaEntityFactory<JobDeviceManagementOperation, JobDeviceManagementOperationCreator, JobDeviceManagementOperationQuery, JobDeviceManagementOperationListResult> {

}
