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
package org.eclipse.kapua.job.engine.jbatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.utils.JobDefinitionBuildUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.internal.JobDomain;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepPredicates;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;

import com.ibm.jbatch.container.jsl.ExecutionElement;
import com.ibm.jbatch.container.jsl.ModelSerializer;
import com.ibm.jbatch.container.jsl.ModelSerializerFactory;
import com.ibm.jbatch.jsl.model.JSLJob;
import com.ibm.jbatch.jsl.model.Step;

@KapuaProvider
public class JobEngineServiceJbatch implements JobEngineService {

    private static final Domain JOB_DOMAIN = new JobDomain();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final JobService JOB_SERVICE = LOCATOR.getService(JobService.class);

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobStepDefinitionService STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    private static final ModelSerializer<JSLJob> JSL_MODEL_SERIALIZER_FACTORY;
    private static final JobOperator JOB_ENGINE;
    private static long jobExecutionId;

    static {

        //
        // jBatch model serializer configuration
        JSL_MODEL_SERIALIZER_FACTORY = ModelSerializerFactory.createJobModelSerializer();

        JOB_ENGINE = BatchRuntime.getJobOperator();
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        Job job = JOB_SERVICE.find(scopeId, jobId);

        // Retrieve job XML definition. Create it if not exists
        String jobXmlDefinition = job.getJobXmlDefinition();
        if (jobXmlDefinition == null) {
            JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(job.getScopeId());
            jobStepQuery.setPredicate(new AttributePredicate<>(JobStepPredicates.JOB_ID, job.getId()));

            JobStepListResult jobSteps = JOB_STEP_SERVICE.query(jobStepQuery);
            jobSteps.sort(Comparator.comparing(JobStep::getStepIndex));

            List<ExecutionElement> jslExecutionElements = new ArrayList<>();
            Iterator<JobStep> jobStepIterator = jobSteps.getItems().iterator();
            while (jobStepIterator.hasNext()) {
                // for (JobStep jobStep : jobSteps.getItems()) {
                JobStep jobStep = jobStepIterator.next();

                Step jslStep = new Step();
                JobStepDefinition jobStepDefinition = STEP_DEFINITION_SERVICE.find(jobStep.getScopeId(), jobStep.getJobStepDefinitionId());
                switch (jobStepDefinition.getStepType()) {
                case GENERIC:
                    jslStep.setBatchlet(JobDefinitionBuildUtils.buildGenericStep(jobStepDefinition));
                    break;
                case TARGET:
                    jslStep.setChunk(JobDefinitionBuildUtils.buildChunkStep(jobStepDefinition));
                    break;
                default:
                    // FIXME: Throw appropriate exception
                    break;
                }

                jslStep.setId("step-" + String.valueOf(jobStep.getStepIndex()));

                if (jobStepIterator.hasNext()) {
                    jslStep.setNextFromAttribute("step-" + String.valueOf(jobStep.getStepIndex() + 1));
                }

                jslStep.setProperties(JobDefinitionBuildUtils.buildStepProperties(jobStepDefinition, jobStep, jobStepIterator.hasNext()));

                jslExecutionElements.add(jslStep);
            }

            JSLJob jslJob = new JSLJob();
            jslJob.setRestartable("true");
            jslJob.setId("job-" + job.getScopeId().toCompactId() + "-" + job.getId().toCompactId());
            jslJob.setVersion("1.0");
            jslJob.setProperties(JobDefinitionBuildUtils.buildJobProperties(job));
            jslJob.setListeners(JobDefinitionBuildUtils.buildListener());
            jslJob.getExecutionElements().addAll(jslExecutionElements);

            jobXmlDefinition = JSL_MODEL_SERIALIZER_FACTORY.serializeModel(jslJob);
            job.setJobXmlDefinition(jobXmlDefinition);
        }

        // Retrieve temporary directory for job XML definition
        String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
        File jobTempDirectory = new File(tmpDirectory, "kapua-job/" + job.getScopeId().toCompactId());
        jobTempDirectory.mkdirs();

        // Retrieve job XML definition file. Delete it if exist
        File jobXmlDefinitionFile = new File(jobTempDirectory, job.getId().toCompactId().concat(".xml"));
        if (jobXmlDefinitionFile.exists()) {
            jobXmlDefinitionFile.delete();
        }

        try (FileOutputStream tmpStream = new FileOutputStream(jobXmlDefinitionFile)) {
            IOUtils.write(jobXmlDefinition, tmpStream);
        } catch (IOException e) {
            throw KapuaException.internalError(e, "Cannot write job XML definition file");
        }

        jobExecutionId = JOB_ENGINE.start(jobXmlDefinitionFile.getAbsolutePath().replaceAll("\\.xml$", ""), new Properties());
    }

    @Override
    public void pauseJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        JOB_ENGINE.stop(jobExecutionId);

    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        JOB_ENGINE.stop(jobExecutionId);
    }

    @Override
    public void resumeJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        JOB_ENGINE.restart(jobExecutionId, null);
    }

}
