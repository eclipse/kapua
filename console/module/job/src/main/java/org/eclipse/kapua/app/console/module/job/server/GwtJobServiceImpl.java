/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtJobServiceImpl extends KapuaRemoteServiceServlet implements GwtJobService {

    @Override
    public PagingLoadResult<GwtJob> query(PagingLoadConfig loadConfig, final GwtJobQuery gwtJobQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtJob> gwtJobs = new ArrayList<GwtJob>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobService jobService = locator.getService(JobService.class);

            // Convert from GWT entity
            JobQuery jobQuery = GwtKapuaJobModelConverter.convertJobQuery(gwtJobQuery, loadConfig);

            // query
            JobListResult jobs = jobService.query(jobQuery);

            // If there are results
            if (!jobs.isEmpty()) {
                final UserService userService = locator.getService(UserService.class);
                final UserFactory userFactory = locator.getFactory(UserFactory.class);
                UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return userService.query(userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobQuery.getScopeId())));
                    }
                });
                Map<String, String> usernameMap = new HashMap<String, String>();
                for (User user : userListResult.getItems()) {
                    usernameMap.put(user.getId().toCompactId(), user.getName());
                }
                // count
                totalLength = Long.valueOf(jobService.count(jobQuery)).intValue();

                // Converto to GWT entity
                for (Job j : jobs.getItems()) {
                    GwtJob gwtJob = KapuaGwtJobModelConverter.convertJob(j);
                    gwtJob.setUserName(usernameMap.get(j.getCreatedBy().toCompactId()));
                    gwtJobs.add(gwtJob);
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
            List<GwtJob> allJobs;
            allJobs = findAll(gwtJobCreator.getScopeId());
            for (GwtJob temp : allJobs) {
                if (temp.getJobName().equals(gwtJobCreator.getName())) {
                    throw new KapuaDuplicateNameException(gwtJobCreator.getName());
                }
            }

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

    public List<GwtJob> findAll(String scopeId) {
        List<GwtJob> jobList = new ArrayList<GwtJob>();
        KapuaLocator locator = KapuaLocator.getInstance();
        JobService jobService = locator.getService(JobService.class);
        JobFactory jobFactory = locator.getFactory(JobFactory.class);
        JobQuery query = jobFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        try {
            JobListResult result = jobService.query(query);
            for (Job job : result.getItems()) {
                jobList.add(KapuaGwtJobModelConverter.convertJob(job));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> findJobDescription(String gwtScopeId,
            String gwtJobId) throws GwtKapuaException {
        List<GwtGroupedNVPair> gwtJobDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            final UserService userService = locator.getService(UserService.class);
            final JobService jobService = locator.getService(JobService.class);
            final KapuaId scopeId = KapuaEid.parseCompactId(gwtScopeId);
            final KapuaId jobId = KapuaEid.parseCompactId(gwtJobId);
            final Job job = jobService.find(scopeId, jobId);
            final User createdUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, job.getCreatedBy());
                }
            });
            final User modifiedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, job.getModifiedBy());
                }
            });

            if (job != null) {
                gwtJobDescription.add(new GwtGroupedNVPair("jobInfo", "jobName", job.getName()));
                gwtJobDescription.add(new GwtGroupedNVPair("jobInfo", "jobDescription", job.getDescription()));
                gwtJobDescription.add(new GwtGroupedNVPair("jobInfo", "jobCreatedOn", KapuaDateUtils.formatDateTime(job.getCreatedOn())));
                gwtJobDescription.add(new GwtGroupedNVPair("jobInfo", "jobCreatedBy", createdUser != null ? createdUser.getName() : null));
                gwtJobDescription.add(new GwtGroupedNVPair("jobInfo", "jobModifiedOn", KapuaDateUtils.formatDateTime(job.getModifiedOn())));
                gwtJobDescription.add(new GwtGroupedNVPair("jobInfo", "jobModifiedBy", modifiedUser != null ? modifiedUser.getName() : null));
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(gwtJobDescription);
    }
}
