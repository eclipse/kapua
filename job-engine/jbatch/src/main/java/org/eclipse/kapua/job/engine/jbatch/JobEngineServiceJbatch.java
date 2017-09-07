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

import javax.batch.operations.JobSecurityException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.BatchRuntime;
//import javax.batch.runtime.BatchStatus;
//import javax.batch.runtime.JobExecution;
//import javax.batch.runtime.JobInstance;
import javax.batch.runtime.BatchStatus;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalStateException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.jbatch.container.jsl.ExecutionElement;
import com.ibm.jbatch.container.jsl.ModelSerializerFactory;
import com.ibm.jbatch.jsl.model.JSLJob;
import com.ibm.jbatch.jsl.model.Step;

@KapuaProvider
public class JobEngineServiceJbatch implements JobEngineService {

    private final static Logger logger = LoggerFactory.getLogger(JobEngineServiceJbatch.class);

    private final static long ON_STOP_MAX_WAIT = 60;
    private final static long ON_STOP_WAIT_STEP = 1000;

    private static final Domain JOB_DOMAIN = new JobDomain();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final JobService JOB_SERVICE = LOCATOR.getService(JobService.class);

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobStepDefinitionService STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        //
        // Check existence
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Start the job

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
            jslJob.setId(getJbatchJobName(job));
            jslJob.setVersion("1.0");
            jslJob.setProperties(JobDefinitionBuildUtils.buildJobProperties(job));
            jslJob.setListeners(JobDefinitionBuildUtils.buildListener());
            jslJob.getExecutionElements().addAll(jslExecutionElements);

            jobXmlDefinition = ModelSerializerFactory.createJobModelSerializer().serializeModel(jslJob);
            job.setJobXmlDefinition(jobXmlDefinition);
            JOB_SERVICE.update(job);
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
        try {
            BatchRuntime.getJobOperator().start(jobXmlDefinitionFile.getAbsolutePath().replaceAll("\\.xml$", ""), new Properties());
        } catch (NoSuchJobExecutionException | NoSuchJobException | JobSecurityException e) {
            String message = String.format("Cannot start job '[%s]': [%s]", jobId, e.getMessage());
            logger.error(message, e);
            throw new KapuaIllegalStateException(message);
        }

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

        //
        // Check existence
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Stop the job
        try {
            Long jbatchJobExecutionId = getRunningJobExecution(getJbatchJobName(job));
            BatchRuntime.getJobOperator().stop(jbatchJobExecutionId);
            // wait for stop completed
            for (int i = 0; i < ON_STOP_MAX_WAIT; i++) {
                if (BatchStatus.STOPPED.equals(BatchRuntime.getJobOperator().getJobExecution(jbatchJobExecutionId).getBatchStatus())) {
                    BatchRuntime.getJobOperator().abandon(jbatchJobExecutionId);
                    break;
                }
                try {
                    Thread.sleep(ON_STOP_WAIT_STEP);
                }
                catch (InterruptedException e) {
                    //ignore it
                }
            }
        } catch (NoSuchJobExecutionException | NoSuchJobException | JobSecurityException e) {
            String message = String.format("Cannot stop job '[%s]': [%s]", jobId, e.getMessage());
            logger.error(message, e);
            throw new KapuaIllegalStateException(message);
        }
    }

    private String getJbatchJobName(Job job) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(job, "job");
        ArgumentValidator.notNull(job.getScopeId(), "job.scopeId");
        ArgumentValidator.notNull(job.getId(), "job.Id");

        return getJbatchJobName(job.getScopeId(), job.getId());
    }

    private String getJbatchJobName(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Generate Jbatch job name
        return String.format("job-%s-%s", scopeId.toCompactId(), jobId.toCompactId());
    }

    private Long getRunningJobExecution(String jobName) {
        List<Long> jobExecutions = getRunningJobExecutions(jobName);
        if (jobExecutions == null || jobExecutions.size() > 1) {
            throw new KapuaIllegalStateException(
                    String.format("Running job [%s] executions count [%d] differs from the expected value [1]", jobName, jobExecutions != null ? jobExecutions.size() : 0));
        }
        return jobExecutions.get(0);
    }

    private List<Long> getRunningJobExecutions(String jobName) {
        return BatchRuntime.getJobOperator().getRunningExecutions(jobName);
    }

    // private long getStoppedJobExecution(String jobName) {
    // List<Long> jobExecutions = getStoppedJobExecutions(jobName);
    // if (jobExecutions.size() != 1) {
    // throw new KapuaIllegalStateException(String.format("Cannot find running job for the specified job name [%s]", jobName));
    // }
    // return jobExecutions.get(0);
    // }
    //
    // private List<Long> getStoppedJobExecutions(String jobName) {
    // List<Long> suspendedJobs = new ArrayList<>();
    // int count = BatchRuntime.getJobOperator().getJobInstanceCount(jobName);
    // int limit = count;
    // // limit to the last few instance
    // if (count > 10) {
    // limit = 10;
    // }
    // List<JobInstance> jobInstances = BatchRuntime.getJobOperator().getJobInstances(jobName, 0, limit);
    // if (jobInstances != null) {
    // for (JobInstance jobInstance : jobInstances) {
    // List<JobExecution> jobExecutions = BatchRuntime.getJobOperator().getJobExecutions(jobInstance);
    //
    // // JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(jobInstance.getInstanceId());
    // jobExecutions.forEach((je) -> {
    // if (BatchStatus.STOPPED.equals(je.getBatchStatus())) {
    // suspendedJobs.add(je.getExecutionId());
    // }
    // });
    //
    // }
    // }
    // return suspendedJobs;
    // }

}
