/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.execution.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;

/**
 * {@link JobExecutionFactory} implementation.
 * 
 * @since 1.0.0
 * 
 */
@KapuaProvider
public class JobExecutionFactoryImpl implements JobExecutionFactory {

    @Override
    public JobExecution newEntity(KapuaId scopeId) {
        return new JobExecutionImpl(scopeId);
    }

    @Override
    public JobExecutionCreator newCreator(KapuaId scopeId) {
        return new JobExecutionCreatorImpl(scopeId);
    }

    @Override
    public JobExecutionQuery newQuery(KapuaId scopeId) {
        return new JobExecutionQueryImpl(scopeId);
    }

    @Override
    public JobExecutionListResult newListResult() {
        return new JobExecutionListResultImpl();
    }

}
