/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.driver;

import com.google.common.collect.Lists;
import com.ibm.jbatch.container.jsl.ExecutionElement;
import com.ibm.jbatch.container.jsl.ModelSerializerFactory;
import com.ibm.jbatch.jsl.model.JSLJob;
import com.ibm.jbatch.jsl.model.Step;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.job.engine.jbatch.driver.exception.JbatchDriverException;
import org.eclipse.kapua.job.engine.jbatch.driver.utils.JbatchUtil;
import org.eclipse.kapua.job.engine.jbatch.setting.KapuaJobEngineSetting;
import org.eclipse.kapua.job.engine.jbatch.setting.KapuaJobEngineSettingKeys;
import org.eclipse.kapua.job.engine.jbatch.utils.JobDefinitionBuildUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
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

import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.operations.NoSuchJobInstanceException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Driver class for Java Batch API
 *
 * @since 1.0.0
 */
public class JbatchDriver {

    private static final Logger LOG = LoggerFactory.getLogger(JbatchDriver.class);

    private static final KapuaJobEngineSetting JOB_ENGINE_SETTING = KapuaJobEngineSetting.getInstance();

    private static final JobOperator JOB_OPERATOR = BatchRuntime.getJobOperator();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobStepDefinitionService STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    private JbatchDriver() {
    }

    /**
     * Builds the jBatch job name from the {@link Job} entity
     *
     * @param job The {@link Job} from which generate the name.
     * @return The jBatch job name
     * @see JbatchDriver#getJbatchJobName(KapuaId, KapuaId)
     * @since 1.0.0
     */
    public static String getJbatchJobName(@NotNull Job job) {
        return getJbatchJobName(job.getScopeId(), job.getId());
    }

    /**
     * Builds the jBatch job name from the {@link Job#getScopeId()} and the {@link Job#getId()}.
     * <p>
     * Format is: job-{scopeIdShort}-{jobIdShort}
     *
     * @param scopeId The scopeId of the {@link Job}
     * @param jobId   The id of the {@link Job}
     * @return The jBatch {@link Job} name
     * @since 1.0.0
     */
    public static String getJbatchJobName(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        return String.format("job-%s-%s", scopeId.toCompactId(), jobId.toCompactId());
    }

    /**
     * Starts a jBatch job with data sourced from the Kapua {@link Job} definition.
     * <p>
     * It builds the XML jBatch job definition using the {@link JSLJob} model definition.
     * The generated XML is store in the {@link SystemUtils#getJavaIoTmpDir()} since the default configuration of jBatch requires a path name to start the jBatch job
     *
     * @param scopeId
     * @param jobId
     * @throws JbatchDriverException
     */
    public static void startJob(KapuaId scopeId, KapuaId jobId) throws JbatchDriverException {

        try {
            JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(scopeId);
            jobStepQuery.setPredicate(new AttributePredicate<>(JobStepPredicates.JOB_ID, jobId));

            JobStepListResult jobSteps = JOB_STEP_SERVICE.query(jobStepQuery);
            jobSteps.sort(Comparator.comparing(JobStep::getStepIndex));

            String jobXmlDefinition = null;
            List<ExecutionElement> jslExecutionElements = new ArrayList<>();
            Iterator<JobStep> jobStepIterator = jobSteps.getItems().iterator();
            while (jobStepIterator.hasNext()) {
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

                jslStep.setId("step-" + jobStep.getStepIndex());

                if (jobStepIterator.hasNext()) {
                    jslStep.setNextFromAttribute("step-" + (jobStep.getStepIndex() + 1));
                }

                jslStep.setProperties(JobDefinitionBuildUtils.buildStepProperties(jobStepDefinition, jobStep, jobStepIterator.hasNext()));

                jslExecutionElements.add(jslStep);
            }

            JSLJob jslJob = new JSLJob();
            jslJob.setRestartable("true");
            jslJob.setId(JbatchDriver.getJbatchJobName(scopeId, jobId));
            jslJob.setVersion("1.0");
            jslJob.setProperties(JobDefinitionBuildUtils.buildJobProperties(scopeId, jobId));
            jslJob.setListeners(JobDefinitionBuildUtils.buildListener());
            jslJob.getExecutionElements().addAll(jslExecutionElements);

            jobXmlDefinition = ModelSerializerFactory.createJobModelSerializer().serializeModel(jslJob);

            // Retrieve temporary directory for job XML definition
            String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
            File jobTempDirectory = new File(tmpDirectory, "kapua-job/" + scopeId.toCompactId());
            if (!jobTempDirectory.exists() && !jobTempDirectory.mkdirs()) {
                throw new JbatchDriverException("Cannot create directory: " + jobTempDirectory.getAbsolutePath());
            }

            // Retrieve job XML definition file. Delete it if exist
            File jobXmlDefinitionFile = new File(jobTempDirectory, jobId.toCompactId().concat(".xml"));
            if (jobXmlDefinitionFile.exists() && !jobXmlDefinitionFile.delete()) {
                throw new JbatchDriverException("Cannot delete directory: " + jobTempDirectory.getAbsolutePath());
            }

            try (FileOutputStream tmpStream = new FileOutputStream(jobXmlDefinitionFile)) {
                IOUtils.write(jobXmlDefinition, tmpStream);
            } catch (IOException e) {
                throw new JbatchDriverException("Cannot write job XML definition file", e);
            }

            if (isRunningJob(scopeId, jobId)) {
                throw new JbatchDriverException(String.format("Cannot start job: [%s]. Job is running!", jobId));
            }

            try {
                JOB_OPERATOR.start(jobXmlDefinitionFile.getAbsolutePath().replaceAll("\\.xml$", ""), new Properties());
            } catch (NoSuchJobExecutionException | NoSuchJobException | JobSecurityException e) {
                throw new JbatchDriverException(String.format("Cannot start job '[%s]': [%s]", jobId, e.getMessage()), e);
            }
        } catch (KapuaException e) {
            throw new JbatchDriverException("Cannot start job", e);
        }
    }

    /**
     * Stops completely the jBatch job.
     * <p>
     * First invokes the {@link JobOperator#stop(long)} on the running execution, which stop the running execution.
     * Secondly, according to the {@link KapuaJobEngineSettingKeys#JOB_ENGINE_STOP_WAIT_CHECK} value, it waits asynchronously the complete stop of the job
     * to be able to invoke {@link JobOperator#abandon(long)} which terminate the jBatch Job.
     * <p>
     * A jBatch job cannot be resumed after this method is invoked on it.
     *
     * @param scopeId The scopeId of the {@link Job}
     * @param jobId   The id of the {@link Job}
     * @throws JbatchDriverException
     */
    public static void stopJob(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) throws JbatchDriverException {

        try {
            JobExecution runningExecution = getRunningJobExecution(scopeId, jobId);

            if (runningExecution != null) {
                JOB_OPERATOR.stop(runningExecution.getExecutionId());

                if (JOB_ENGINE_SETTING.getBoolean(KapuaJobEngineSettingKeys.JOB_ENGINE_STOP_WAIT_CHECK)) {
                    JbatchUtil.waitForStop(runningExecution, () -> JOB_OPERATOR.abandon(runningExecution.getExecutionId()));
                }
            }
        } catch (NoSuchJobExecutionException e) {
            throw new JbatchDriverException("Cannot find a job running execution", e);
        } catch (JobExecutionNotRunningException e) {
            throw new JbatchDriverException("Execution is already stopped", e);
        }

    }

    /**
     * Checks whether or not the {@link Job} identified by the parametersis in a running status.
     * <p>
     * jBatch {@link Job} running statuses are listed in {@link JbatchJobRunningStatuses}
     *
     * @param scopeId The scopeId of the {@link Job}
     * @param jobId   The id of the {@link Job}
     * @return {@code true} if the jBatch {@link Job} is running, {@code false} otherwise,
     * @throws JbatchDriverException
     */
    public static boolean isRunningJob(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        return getRunningJobExecution(scopeId, jobId) != null;
    }

    //
    // Private methods
    //
    private static JobExecution getRunningJobExecution(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        return getJobExecutions(scopeId, jobId).stream().filter(je -> JbatchJobRunningStatuses.getStatuses().contains(je.getBatchStatus())).findFirst().orElse(null);
    }

    private static List<JobExecution> getJobExecutions(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        List<JobExecution> jobExecutions = Lists.newArrayList();

        String jobName = getJbatchJobName(scopeId, jobId);
        try {
            int jobInstanceCount = JOB_OPERATOR.getJobInstanceCount(jobName);
            List<JobInstance> jobInstances = JOB_OPERATOR.getJobInstances(jobName, 0, jobInstanceCount);
            jobInstances.forEach(ji -> jobExecutions.addAll(getJbatchJobExecutions(ji)));
        } catch (NoSuchJobException e) {
            LOG.debug("Error while getting Job: {}", e, jobName);
            // This exception is thrown when there is no job, this means that the job never run before
            // So we can ignore it and return `null`
        } catch (NoSuchJobExecutionException e) {
            LOG.debug("Error while getting execution status for Job: {}", e, jobName);
            // This exception is thrown when there is no execution is running.
            // So we can ignore it and return `null`
        }

        return jobExecutions;
    }

    private static List<JobExecution> getJbatchJobExecutions(@NotNull JobInstance jobInstance) {
        try {
            return JOB_OPERATOR.getJobExecutions(jobInstance);
        } catch (NoSuchJobInstanceException e) {
            LOG.debug("Error while getting Job Instance: {}", e, jobInstance.getInstanceId());
            // This exception is thrown when there is no job instance, this means that the job never run before
            // So we can ignore it and return `null`
        }

        return Collections.emptyList();
    }
}
