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
package org.eclipse.kapua.job.engine.jbatch.step;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import org.eclipse.kapua.job.engine.jbatch.step.definition.LogPropertyKeys;
import org.eclipse.kapua.service.job.commons.operation.AbstractGenericOperation;
import org.eclipse.kapua.service.job.operation.GenericOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogProcessor extends AbstractGenericOperation implements GenericOperation {

    private static final Logger LOG = LoggerFactory.getLogger(LogProcessor.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    public void processInternal() throws Exception {

        String logString = getStepContext().getStepProperty(LogPropertyKeys.LOG_STRING, String.class);

        LOG.info("JOB {} - {}", getJobContext().getJobId(), logString);
    }

    @Override
    protected void retrieveContext() {
        setContext(jobContext, stepContext);
    }

}
