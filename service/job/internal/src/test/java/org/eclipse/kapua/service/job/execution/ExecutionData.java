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
package org.eclipse.kapua.service.job.execution;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionListResultImpl;

import javax.inject.Singleton;

@Singleton
public class ExecutionData {

    // Step scratchpad data
    public JobExecution execution;
    public JobExecution foundExecution;
    public JobExecutionCreator executionCreator;
    public JobExecutionListResult resultList;
    public KapuaId currentExecutionId;

    public ExecutionData() {
        cleanup();
    }

    public void cleanup() {
        executionCreator = null;
        execution = null;
        foundExecution = null;
        resultList = new JobExecutionListResultImpl();
        currentExecutionId = null;
    }
}
