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

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;

import java.util.ArrayList;
import java.util.List;

public class GwtJobStepServiceImpl extends KapuaRemoteServiceServlet implements GwtJobStepService {

    @Override
    public PagingLoadResult<GwtJobStep> query(PagingLoadConfig loadConfig, GwtJobStepQuery gwtJobStepQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtJobStep> gwtJobStepList = new ArrayList<GwtJobStep>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepService jobStepService = locator.getService(JobStepService.class);

            // Convert from GWT entity
            JobStepQuery jobStepQuery = GwtKapuaJobModelConverter.convertJobStepQuery(gwtJobStepQuery, loadConfig);

            // query
            JobStepListResult jobStepList = jobStepService.query(jobStepQuery);

            // If there are results
            if (!jobStepList.isEmpty()) {
                // count
                if (jobStepList.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(jobStepService.count(jobStepQuery)).intValue();
                } else {
                    totalLength = jobStepList.getSize();
                }

                // Converto to GWT entity
                for (JobStep js : jobStepList.getItems()) {
                    gwtJobStepList.add(KapuaGwtJobModelConverter.convertJobStep(js));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtJobStep>(gwtJobStepList, loadConfig.getOffset(), totalLength);
    }

    @Override
    public PagingLoadResult<GwtJobStep> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId) throws GwtKapuaException {
        if (jobId != null) {
            GwtJobStepQuery gwtJobStepQuery = new GwtJobStepQuery();
            gwtJobStepQuery.setScopeId(scopeId);
            gwtJobStepQuery.setJobId(jobId);
            return query(loadConfig, gwtJobStepQuery);
        } else {
            return new BasePagingLoadResult<GwtJobStep>(new ArrayList<GwtJobStep>(), 0, 0);
        }
    }

    @Override
    public GwtJobStep create(GwtXSRFToken xsrfToken, GwtJobStepCreator gwtJobStepCreator) throws GwtKapuaException {
        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtJobStep gwtJobStep = null;
        try {
            // Convert from GWT Entity
            JobStepCreator jobStepCreator = GwtKapuaJobModelConverter.convertJobStepCreator(gwtJobStepCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepService jobStepService = locator.getService(JobStepService.class);
            JobStep jobStep = jobStepService.create(jobStepCreator);

            // Convert
            gwtJobStep = KapuaGwtJobModelConverter.convertJobStep(jobStep);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtJobStep;
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtJobStepId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobTargetId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepId);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepService jobTargetService = locator.getService(JobStepService.class);
            jobTargetService.delete(scopeId, jobTargetId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtJobStep find(String gwtScopeId, String gwtJobStepId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(gwtScopeId);
        KapuaId jobStepId = KapuaEid.parseCompactId(gwtJobStepId);

        GwtJobStep gwtJobStep = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepService gwtJobStepService = locator.getService(JobStepService.class);
            JobStep jobStep = gwtJobStepService.find(scopeId, jobStepId);
            if (jobStep != null) {
                gwtJobStep = KapuaGwtJobModelConverter.convertJobStep(jobStep);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtJobStep;
    }

    @Override
    public int getFirstFreeStepIndex(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        BasePagingLoadConfig loadConfig = new BasePagingLoadConfig();
        GwtJobStepQuery query = new GwtJobStepQuery();
        query.setScopeId(gwtScopeId);
        query.setJobId(gwtJobId);
        query.setSortAttribute(GwtJobStepQuery.GwtSortAttribute.STEP_INDEX);
        query.setSortOrder(GwtJobStepQuery.GwtSortOrder.DESCENDING);
        PagingLoadResult<GwtJobStep> result = query(loadConfig, query);
        if (result.getData().size() > 0) {
            return result.getData().get(0).getStepIndex() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public GwtJobStep update(GwtXSRFToken xsrfToken, GwtJobStep gwtJobStep) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtJobStep gwtJobStepUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepService jobStepService = locator.getService(JobStepService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtJobStep.getScopeId());
            KapuaId userId = KapuaEid.parseCompactId(gwtJobStep.getId());

            JobStep jobStep = jobStepService.find(scopeId, userId);

            if (jobStep != null) {

                //
                // Update job step
                jobStep.setName(gwtJobStep.getJobStepName());
                jobStep.setDescription(gwtJobStep.getDescription());
                jobStep.setJobStepDefinitionId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getJobStepDefinitionId()));
                jobStep.setStepProperties(GwtKapuaJobModelConverter.convertJobStepProperties(gwtJobStep.getStepProperties()));

                // optlock
                jobStep.setOptlock(gwtJobStep.getOptlock());

                // update the user
                JobStep jobStepUpdated = jobStepService.update(jobStep);

                //
                // convert to GwtAccount and return
                // reload the user as we want to load all its permissions
                gwtJobStepUpdated = KapuaGwtJobModelConverter.convertJobStep(jobStepUpdated);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtJobStepUpdated;
    }

    @Override
    public GwtJobStepProperty trickGwt() {
        return null;
    }
}
