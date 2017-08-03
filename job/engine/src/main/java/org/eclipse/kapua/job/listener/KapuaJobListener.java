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
package org.eclipse.kapua.job.listener;

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.api.listener.JobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.context.JobContextFactory;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaJobListener extends AbstractJobListener implements JobListener {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaJobListener.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobContextFactory JOB_CONTEXT_FACTORY = LOCATOR.getFactory(JobContextFactory.class);

    @Inject
    private JobContext jobContext;

    @Override
    public void beforeJob() throws Exception {
        KapuaJobContext kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);

        LOG.info("JOB {} - Running before job...", kapuaJobContext.getJobId());
        LOG.info("JOB {} - Running before job... DONE!", kapuaJobContext.getJobId());
    }

    @Override
    public void afterJob() throws Exception {
        KapuaJobContext kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);

        LOG.info("JOB {} - Running after job...", kapuaJobContext.getJobId());
        LOG.info("JOB {} - Running after job... DONE!", kapuaJobContext.getJobId());
    }
}
