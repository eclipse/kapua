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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;

/**
 * {@link JobFactory} implementation.
 * 
 * @since 1.0.0
 * 
 */
@KapuaProvider
public class JobFactoryImpl implements JobFactory {

    @Override
    public Job newEntity(KapuaId scopeId) {
        return new JobImpl(scopeId);
    }

    @Override
    public JobCreator newCreator(KapuaId scopeId) {
        return new JobCreatorImpl(scopeId);
    }

    @Override
    public JobQuery newQuery(KapuaId scopeId) {
        return new JobQueryImpl(scopeId);
    }

    @Override
    public JobListResult newListResult() {
        return new JobListResultImpl();
    }
}
