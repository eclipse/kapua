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
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionRepository;
import org.eclipse.kapua.storage.TxContext;

public class JobStepDefinitionImplJpaRepository
        extends KapuaNamedEntityJpaRepository<JobStepDefinition, JobStepDefinitionImpl, JobStepDefinitionListResult>
        implements JobStepDefinitionRepository {

    public JobStepDefinitionImplJpaRepository() {
        super(JobStepDefinitionImpl.class, () -> new JobStepDefinitionListResultImpl());
    }

    @Override
    public JobStepDefinition delete(TxContext tx, KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        final JobStepDefinition toDelete = this.find(tx, scopeId, stepDefinitionId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(JobStepDefinition.TYPE, stepDefinitionId));

        return this.delete(tx, toDelete);
    }
}
