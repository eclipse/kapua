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
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

/**
 * {@link JobStepDefinitionFactory} definition.
 * 
 * @since 1.0
 * 
 */
@KapuaProvider
public class JobStepDefinitionFactoryImpl implements JobStepDefinitionFactory {

    @Override
    public JobStepDefinition newEntity(KapuaId scopeId) {
        return new JobStepDefinitionImpl(scopeId);
    }

    @Override
    public JobStepDefinitionCreator newCreator(KapuaId scopeId) {
        return new JobStepDefinitionCreatorImpl(scopeId);
    }

    @Override
    public JobStepDefinitionQuery newQuery(KapuaId scopeId) {
        return new JobStepDefinitionQueryImpl(scopeId);
    }

    @Override
    public JobStepDefinitionListResult newListResult() {
        return new JobStepDefinitionListResultImpl();
    }

    @Override
    public JobStepProperty newStepProperty(String name, String type, String value) {
        return new JobStepPropertyImpl(name, type, value);
    }

}
