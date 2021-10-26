/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionFactoryImpl;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionServiceImpl;
import org.eclipse.kapua.service.job.internal.JobCreatorImpl;
import org.eclipse.kapua.service.job.internal.JobFactoryImpl;
import org.eclipse.kapua.service.job.internal.JobServiceImpl;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionFactoryImpl;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionServiceImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepCreatorImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepFactoryImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepServiceImpl;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.internal.JobTargetCreatorImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetFactoryImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetServiceImpl;

public class JobModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobExecutionFactory.class).to(JobExecutionFactoryImpl.class);
        bind(JobExecutionService.class).to(JobExecutionServiceImpl.class);

        bind(JobCreator.class).to(JobCreatorImpl.class);
        bind(JobFactory.class).to(JobFactoryImpl.class);
        bind(JobService.class).to(JobServiceImpl.class);

        bind(JobStepDefinitionFactory.class).to(JobStepDefinitionFactoryImpl.class);
        bind(JobStepDefinitionService.class).to(JobStepDefinitionServiceImpl.class);

        bind(JobStepCreator.class).to(JobStepCreatorImpl.class);
        bind(JobStepFactory.class).to(JobStepFactoryImpl.class);
        bind(JobStepService.class).to(JobStepServiceImpl.class);

        bind(JobTargetCreator.class).to(JobTargetCreatorImpl.class);
        bind(JobTargetFactory.class).to(JobTargetFactoryImpl.class);
        bind(JobTargetService.class).to(JobTargetServiceImpl.class);
    }
}
