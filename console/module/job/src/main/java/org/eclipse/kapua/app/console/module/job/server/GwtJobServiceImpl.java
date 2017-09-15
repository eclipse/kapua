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
package org.eclipse.kapua.app.console.module.job.server;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;

import java.util.ArrayList;
import java.util.List;

public class GwtJobServiceImpl extends KapuaRemoteServiceServlet implements GwtJobService {

    @Override
    public PagingLoadResult<GwtJob> query(PagingLoadConfig loadConfig, GwtJobQuery gwtJobQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtJob> gwtJobs = new ArrayList<GwtJob>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobService jobService = locator.getService(JobService.class);

            // Convert from GWT entity
            JobQuery userQuery = GwtKapuaJobModelConverter.convertJobQuery(gwtJobQuery, loadConfig);

            // query
            JobListResult jobs = jobService.query(userQuery);

            // If there are results
            if (!jobs.isEmpty()) {
                // count
                if (jobs.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(jobService.count(userQuery)).intValue();
                } else {
                    totalLength = jobs.getSize();
                }

                // Converto to GWT entity
                for (Job j : jobs.getItems()) {
                    gwtJobs.add(KapuaGwtJobModelConverter.convertJob(j));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtJob>(gwtJobs, loadConfig.getOffset(), totalLength);
    }

    @Override
    public GwtJob create(GwtXSRFToken xsrfToken, GwtJobCreator gwtJobCreator) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtJob gwtJob = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobFactory jobFactory = locator.getFactory(JobFactory.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtJobCreator.getScopeId());
            JobCreator jobCreator = jobFactory.newCreator(scopeId);
            jobCreator.setName(gwtJobCreator.getName());
            jobCreator.setDescription(gwtJobCreator.getDescription());

            //
            // Create the Job
            JobService jobService = locator.getService(JobService.class);
            Job job = jobService.create(jobCreator);

            // convert to GwtJob and return
            gwtJob = KapuaGwtJobModelConverter.convertJob(job);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtJob;
    }

    @Override
    public GwtJob find(String accountId, String jobIdString) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(accountId);
        KapuaId jobId = KapuaEid.parseCompactId(jobIdString);

        GwtJob gwtJob = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobService jobService = locator.getService(JobService.class);
            Job job = jobService.find(scopeId, jobId);
            if (job != null) {
                gwtJob = KapuaGwtJobModelConverter.convertJob(job);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtJob;
    }

    @Override
    public GwtJob update(GwtXSRFToken xsrfToken, GwtJob gwtJob) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtJob gwtJobUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobService jobService = locator.getService(JobService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtJob.getScopeId());
            KapuaId jobId = KapuaEid.parseCompactId(gwtJob.getId());

            Job job = jobService.find(scopeId, jobId);

            if (job != null) {

                //
                // Update job
                job.setName(gwtJob.getJobName());
                job.setDescription(gwtJob.getDescription());

                // optlock
                job.setOptlock(gwtJob.getOptlock());

                // update the job
                gwtJobUpdated = KapuaGwtJobModelConverter.convertJob(jobService.update(job));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtJobUpdated;

    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobId);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobService jobService = locator.getService(JobService.class);
            jobService.delete(scopeId, jobId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }
}
