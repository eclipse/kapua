/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaTransactedRepository;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class JobStepImplJpaTransactedRepository
        extends KapuaNamedEntityJpaTransactedRepository<JobStep, JobStepImpl, JobStepListResult>
        implements JobStepTransactedRepository {
    public JobStepImplJpaTransactedRepository(TxManager txManager, Supplier<JobStepListResult> listSupplier) {
        super(txManager, JobStepImpl.class, listSupplier);
    }
}
