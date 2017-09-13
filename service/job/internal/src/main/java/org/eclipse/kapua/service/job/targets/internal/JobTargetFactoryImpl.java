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
package org.eclipse.kapua.service.job.targets.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;

/**
 * {@link JobTargetFactory} implementation.
 * 
 * @since 1.0.0
 * 
 */
@KapuaProvider
public class JobTargetFactoryImpl implements JobTargetFactory {

    @Override
    public JobTarget newEntity(KapuaId scopeId) {
        return new JobTargetImpl(scopeId);
    }

    @Override
    public JobTargetCreator newCreator(KapuaId scopeId) {
        return new JobTargetCreatorImpl(scopeId);
    }

    @Override
    public JobTargetQuery newQuery(KapuaId scopeId) {
        return new JobTargetQueryImpl(scopeId);
    }

    @Override
    public JobTargetListResult newListResult() {
        return new JobTargetListResultImpl();
    }

}
