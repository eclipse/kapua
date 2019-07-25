/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.job.steps;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.job.engine.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.service.job.operation.TargetProcessor;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class TestProcessor extends AbstractTargetProcessor implements TargetProcessor {

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    protected void initProcessing(JobTargetWrapper wrappedJobTarget) {
        setContext(jobContext, stepContext);
    }

    @Override
    public void processTarget(JobTarget jobTarget) throws KapuaException {

        boolean fail = Boolean.parseBoolean(System.getProperty("testJobDefinitionProcessorFail", "false"));

        if (fail) {
            throw KapuaException.internalError("This processing has been set to fail");
        }
    }
}