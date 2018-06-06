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

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepQuery;
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
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;

import java.util.ArrayList;
import java.util.List;

public class GwtJobStepServiceImpl extends KapuaRemoteServiceServlet implements GwtJobStepService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepDefinitionService JOB_STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    @Override
    public PagingLoadResult<GwtJobStep> query(PagingLoadConfig loadConfig, GwtJobStepQuery gwtJobStepQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtJobStep> gwtJobStepList = new ArrayList<GwtJobStep>();
        try {
            // Convert from GWT entity
            JobStepQuery jobStepQuery = GwtKapuaJobModelConverter.convertJobStepQuery(gwtJobStepQuery, loadConfig);

            // query
            JobStepListResult jobStepList = JOB_STEP_SERVICE.query(jobStepQuery);
            totalLength = (int) JOB_STEP_SERVICE.count(jobStepQuery);

            // Converto to GWT entity
            for (JobStep js : jobStepList.getItems()) {
                GwtJobStep gwtJobStep = KapuaGwtJobModelConverter.convertJobStep(js);

                JobStepDefinition jobStepDefinition = JOB_STEP_DEFINITION_SERVICE
                        .find(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getScopeId()), GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getJobStepDefinitionId()));
                gwtJobStep.setJobStepDefinitionName(jobStepDefinition.getName());

                setEnumOnJobStepProperty(gwtJobStep.getStepProperties());

                gwtJobStepList.add(gwtJobStep);
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
            JobStep jobStep = JOB_STEP_SERVICE.create(jobStepCreator);

            // Convert
            gwtJobStep = KapuaGwtJobModelConverter.convertJobStep(jobStep);

            setEnumOnJobStepProperty(gwtJobStep.getStepProperties());
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
            JOB_STEP_SERVICE.delete(scopeId, jobTargetId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtJobStep find(String gwtScopeId, String gwtJobStepId) throws GwtKapuaException {

        GwtJobStep gwtJobStep = null;
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtScopeId);
            KapuaId jobStepId = KapuaEid.parseCompactId(gwtJobStepId);

            JobStep jobStep = JOB_STEP_SERVICE.find(scopeId, jobStepId);

            if (jobStep != null) {
                gwtJobStep = KapuaGwtJobModelConverter.convertJobStep(jobStep);

                setEnumOnJobStepProperty(gwtJobStep.getStepProperties());
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtJobStep;
    }

    @Override
    public GwtJobStep update(GwtXSRFToken xsrfToken, GwtJobStep gwtJobStep) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtJobStep gwtJobStepUpdated = null;
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtJobStep.getScopeId());
            KapuaId userId = KapuaEid.parseCompactId(gwtJobStep.getId());

            JobStep jobStep = JOB_STEP_SERVICE.find(scopeId, userId);

            if (jobStep != null) {

                //
                // Update job step
                jobStep.setName(gwtJobStep.getUnescapedJobStepName());
                jobStep.setDescription(gwtJobStep.getUnescapedDescription());
                jobStep.setJobStepDefinitionId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getJobStepDefinitionId()));
                jobStep.setStepProperties(GwtKapuaJobModelConverter.convertJobStepProperties(gwtJobStep.getStepProperties()));

                // optlock
                jobStep.setOptlock(gwtJobStep.getOptlock());

                // update the user
                JobStep jobStepUpdated = JOB_STEP_SERVICE.update(jobStep);

                //
                // convert to GwtAccount and return
                // reload the user as we want to load all its permissions
                gwtJobStepUpdated = KapuaGwtJobModelConverter.convertJobStep(jobStepUpdated);

                setEnumOnJobStepProperty(gwtJobStep.getStepProperties());
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtJobStepUpdated;
    }

    /**
     * Set the {@link GwtJobStepProperty#isEnum()} property.
     * This cannot be performed in *.shared.* packages (entity converters are in that package), since `Class.forName` is not present in the JRE Emulation library.
     *
     * @param jobStepProperties
     * @throws ClassNotFoundException
     */
    private void setEnumOnJobStepProperty(List<GwtJobStepProperty> jobStepProperties) throws ClassNotFoundException {
        for (GwtJobStepProperty gjsp : jobStepProperties) {
            gjsp.setEnum(Class.forName(gjsp.getPropertyType()).isEnum());
        }
    }

    @Override
    public GwtJobStepProperty trickGwt() {
        return null;
    }
}
