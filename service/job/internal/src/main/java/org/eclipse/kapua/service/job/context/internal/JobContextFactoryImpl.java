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
package org.eclipse.kapua.service.job.context.internal;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.job.context.JobContextFactory;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.KapuaStepContext;

@KapuaProvider
public class JobContextFactoryImpl implements JobContextFactory {

    @Override
    public KapuaJobContext newJobContext(JobContext jobContext) {
        return new KapuaJobContextImpl(jobContext);
    }

    @Override
    public KapuaStepContext newStepContext(StepContext stepContext) {
        return new KapuaStepContextImpl(stepContext);
    }
}
