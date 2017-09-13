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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepPropertyImpl;

/**
 * {@link JobStepFactory} implementation.
 * 
 * @since 1.0.0
 * 
 */
@KapuaProvider
public class JobStepFactoryImpl implements JobStepFactory {

    @Override
    public JobStep newEntity(KapuaId scopeId) {
        return new JobStepImpl(scopeId);
    }

    @Override
    public JobStepCreator newCreator(KapuaId scopeId) {
        return new JobStepCreatorImpl(scopeId);
    }

    @Override
    public JobStepQuery newQuery(KapuaId scopeId) {
        return new JobStepQueryImpl(scopeId);
    }

    @Override
    public JobStepListResult newListResult() {
        return new JobStepListResultImpl();
    }

    @Override
    public JobStepProperty newStepProperty(String name, String propertyType, String propertyValue) {
        return new JobStepPropertyImpl(name, propertyType, propertyValue);
    }
}
