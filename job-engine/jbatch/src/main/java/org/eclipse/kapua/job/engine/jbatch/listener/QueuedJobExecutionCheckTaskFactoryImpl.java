/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.listener;

import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.setting.JobEngineSetting;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionFactory;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionService;
import org.eclipse.kapua.model.id.KapuaId;

import javax.inject.Inject;

public class QueuedJobExecutionCheckTaskFactoryImpl implements QueuedJobExecutionCheckTaskFactory {
    private final JobEngineSetting jobEngineSetting;
    private final JobEngineService jobEngineService;
    private final QueuedJobExecutionService queuedJobExecutionService;
    private final QueuedJobExecutionFactory queuedJobExecutionFactory;

    @Inject
    public QueuedJobExecutionCheckTaskFactoryImpl(
            JobEngineSetting jobEngineSetting,
            JobEngineService jobEngineService,
            QueuedJobExecutionService queuedJobExecutionService,
            QueuedJobExecutionFactory queuedJobExecutionFactory) {
        this.jobEngineSetting = jobEngineSetting;
        this.jobEngineService = jobEngineService;
        this.queuedJobExecutionService = queuedJobExecutionService;
        this.queuedJobExecutionFactory = queuedJobExecutionFactory;
    }

    @Override
    public QueuedJobExecutionCheckTask create(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) {
        return new QueuedJobExecutionCheckTask(jobEngineSetting, jobEngineService, queuedJobExecutionService, queuedJobExecutionFactory, scopeId, jobId, jobExecutionId);
    }
}
