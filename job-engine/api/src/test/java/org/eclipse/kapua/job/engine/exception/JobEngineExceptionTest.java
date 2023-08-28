/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.job.engine.exception.model.TestCodesJobEngineException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link JobEngineException}s tests.
 *
 * @since 2.0.0
 */
@Category(JUnitTests.class)
public class JobEngineExceptionTest {

    private final Throwable aCause = new Throwable("This is the cause");
    private final KapuaId aScopeId = new KapuaIdImpl(new BigInteger("1"));
    private final KapuaId aJobId = new KapuaIdImpl(new BigInteger("2"));
    private final KapuaId aJobExecutionId = new KapuaIdImpl(new BigInteger("3"));
    private final Set<KapuaId> aJobTargetIdSet = Stream.of(
                    new KapuaIdImpl(new BigInteger("4")),
                    new KapuaIdImpl(new BigInteger("5")))
            .collect(Collectors.toSet());


    @Test
    public void testJobEngineErrorCodesHaveMessages() {
        for (JobEngineErrorCodes errorCode : JobEngineErrorCodes.values()) {
            TestCodesJobEngineException jobEngineException = new TestCodesJobEngineException(errorCode);

            Assert.assertNotEquals("TransportErrorCodes." + errorCode + " doesn't have an error message", "Error: ", jobEngineException.getMessage());
            Assert.assertNotEquals("TransportErrorCodes." + errorCode + " doesn't have an error message", "Error: ", jobEngineException.getLocalizedMessage());
        }
    }

    @Test
    public void testTransportClientGetException() {
        String exceptionMessage = "The Job Engine data for Job " + aJobId + " in scope " + aScopeId + " cannot be cleaned up.";

        // Without cause
        CleanJobDataException cleanJobDataException = new CleanJobDataException(aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.CANNOT_CLEANUP_JOB_DATA, cleanJobDataException.getCode());
        Assert.assertNull(cleanJobDataException.getCause());
        Assert.assertEquals(aScopeId, cleanJobDataException.getScopeId());
        Assert.assertEquals(aJobId, cleanJobDataException.getJobId());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getMessage());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getLocalizedMessage());

        // With cause
        exceptionMessage = exceptionMessage + " Caused by: " + aCause.getMessage();

        cleanJobDataException = new CleanJobDataException(aCause, aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.CANNOT_CLEANUP_JOB_DATA_WITH_CAUSE, cleanJobDataException.getCode());
        Assert.assertNotNull(cleanJobDataException.getCause());
        Assert.assertEquals(aCause, cleanJobDataException.getCause());
        Assert.assertEquals(aScopeId, cleanJobDataException.getScopeId());
        Assert.assertEquals(aScopeId, cleanJobDataException.getScopeId());
        Assert.assertEquals(aJobId, cleanJobDataException.getJobId());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getMessage());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getLocalizedMessage());
    }

    @Test
    public void testJobAlreadyRunningException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " cannot be started because it is already running.";

        JobAlreadyRunningException jobAlreadyRunningException = new JobAlreadyRunningException(aScopeId, aJobId, aJobExecutionId, aJobTargetIdSet);

        Assert.assertEquals(JobEngineErrorCodes.JOB_ALREADY_RUNNING, jobAlreadyRunningException.getCode());
        Assert.assertNull(jobAlreadyRunningException.getCause());
        Assert.assertEquals(aScopeId, jobAlreadyRunningException.getScopeId());
        Assert.assertEquals(aJobId, jobAlreadyRunningException.getJobId());
        Assert.assertEquals(aJobExecutionId, jobAlreadyRunningException.getJobExecutionId());
        Assert.assertEquals(aJobTargetIdSet, jobAlreadyRunningException.getJobTargetIdSubset());
        Assert.assertEquals(exceptionMessage, jobAlreadyRunningException.getMessage());
        Assert.assertEquals(exceptionMessage, jobAlreadyRunningException.getLocalizedMessage());
    }

    @Test
    public void testJobCheckRunningException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " cannot be checked for running status.";

        JobCheckRunningException jobCheckRunningException = new JobCheckRunningException(aCause, aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_CHECK_RUNNING, jobCheckRunningException.getCode());
        Assert.assertNotNull(jobCheckRunningException.getCause());
        Assert.assertEquals(aCause, jobCheckRunningException.getCause());
        Assert.assertEquals(aScopeId, jobCheckRunningException.getScopeId());
        Assert.assertEquals(aJobId, jobCheckRunningException.getJobId());
        Assert.assertEquals(exceptionMessage, jobCheckRunningException.getMessage());
        Assert.assertEquals(exceptionMessage, jobCheckRunningException.getLocalizedMessage());
    }

    @Test
    public void testJobInvalidTargetException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " cannot be started because at least one of the " + aJobTargetIdSet.size() + " Job Target is not a current Job Target.";

        JobInvalidTargetException jobInvalidTargetException = new JobInvalidTargetException(aScopeId, aJobId, aJobTargetIdSet);

        Assert.assertEquals(JobEngineErrorCodes.JOB_TARGET_INVALID, jobInvalidTargetException.getCode());
        Assert.assertNull(jobInvalidTargetException.getCause());
        Assert.assertEquals(aScopeId, jobInvalidTargetException.getScopeId());
        Assert.assertEquals(aJobId, jobInvalidTargetException.getJobId());
        Assert.assertEquals(aJobTargetIdSet, jobInvalidTargetException.getTargetSublist());
        Assert.assertEquals(exceptionMessage, jobInvalidTargetException.getMessage());
        Assert.assertEquals(exceptionMessage, jobInvalidTargetException.getLocalizedMessage());
    }

    @Test
    public void testJobMissingStepException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " does not have steps configured.";

        JobMissingStepException jobMissingStepException = new JobMissingStepException(aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_STEP_MISSING, jobMissingStepException.getCode());
        Assert.assertNull(jobMissingStepException.getCause());
        Assert.assertEquals(aScopeId, jobMissingStepException.getScopeId());
        Assert.assertEquals(aJobId, jobMissingStepException.getJobId());
        Assert.assertEquals(exceptionMessage, jobMissingStepException.getMessage());
        Assert.assertEquals(exceptionMessage, jobMissingStepException.getLocalizedMessage());
    }

    @Test
    public void testJobMissingTargetException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " does not have targets configured.";

        JobMissingTargetException jobMissingTargetException = new JobMissingTargetException(aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_TARGET_MISSING, jobMissingTargetException.getCode());
        Assert.assertNull(jobMissingTargetException.getCause());
        Assert.assertEquals(aScopeId, jobMissingTargetException.getScopeId());
        Assert.assertEquals(aJobId, jobMissingTargetException.getJobId());
        Assert.assertEquals(exceptionMessage, jobMissingTargetException.getMessage());
        Assert.assertEquals(exceptionMessage, jobMissingTargetException.getLocalizedMessage());
    }

    @Test
    public void testJobNotRunningException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " cannot be stopped because it is not running.";

        JobNotRunningException jobNotRunningException = new JobNotRunningException(aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_NOT_RUNNING, jobNotRunningException.getCode());
        Assert.assertNull(jobNotRunningException.getCause());
        Assert.assertEquals(aScopeId, jobNotRunningException.getScopeId());
        Assert.assertEquals(aJobId, jobNotRunningException.getJobId());
        Assert.assertEquals(exceptionMessage, jobNotRunningException.getMessage());
        Assert.assertEquals(exceptionMessage, jobNotRunningException.getLocalizedMessage());
    }

    @Test
    public void testJobResumingException() {
        String exceptionMessage = "The Job Execution " + aJobExecutionId + " of Job " + aJobId + " in scope " + aScopeId + " cannot be resumed.";

        // Without cause
        JobResumingException cleanJobDataException = new JobResumingException(aScopeId, aJobId, aJobExecutionId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_RESUMING, cleanJobDataException.getCode());
        Assert.assertNull(cleanJobDataException.getCause());
        Assert.assertEquals(aScopeId, cleanJobDataException.getScopeId());
        Assert.assertEquals(aJobId, cleanJobDataException.getJobId());
        Assert.assertEquals(aJobExecutionId, cleanJobDataException.getJobExecutionId());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getMessage());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getLocalizedMessage());

        // With cause
        cleanJobDataException = new JobResumingException(aCause, aScopeId, aJobId, aJobExecutionId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_RESUMING, cleanJobDataException.getCode());
        Assert.assertNotNull(cleanJobDataException.getCause());
        Assert.assertEquals(aScopeId, cleanJobDataException.getScopeId());
        Assert.assertEquals(aScopeId, cleanJobDataException.getScopeId());
        Assert.assertEquals(aJobId, cleanJobDataException.getJobId());
        Assert.assertEquals(aJobExecutionId, cleanJobDataException.getJobExecutionId());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getMessage());
        Assert.assertEquals(exceptionMessage, cleanJobDataException.getLocalizedMessage());
    }

    @Test
    public void testJobRunningException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " is running and the operation cannot be performed.";

        JobRunningException jobRunningException = new JobRunningException(aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_RUNNING, jobRunningException.getCode());
        Assert.assertNull(jobRunningException.getCause());
        Assert.assertEquals(aScopeId, jobRunningException.getScopeId());
        Assert.assertEquals(aJobId, jobRunningException.getJobId());
        Assert.assertEquals(exceptionMessage, jobRunningException.getMessage());
        Assert.assertEquals(exceptionMessage, jobRunningException.getLocalizedMessage());
    }

    @Test
    public void testJobStartingException() {
        String exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " cannot be started.";

        // Without cause
        JobStartingException jobStartingException = new JobStartingException(aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_STARTING, jobStartingException.getCode());
        Assert.assertNull(jobStartingException.getCause());
        Assert.assertEquals(aScopeId, jobStartingException.getScopeId());
        Assert.assertEquals(aJobId, jobStartingException.getJobId());
        Assert.assertEquals(exceptionMessage, jobStartingException.getMessage());
        Assert.assertEquals(exceptionMessage, jobStartingException.getLocalizedMessage());

        // With cause
        jobStartingException = new JobStartingException(aCause, aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_STARTING, jobStartingException.getCode());
        Assert.assertNotNull(jobStartingException.getCause());
        Assert.assertEquals(aScopeId, jobStartingException.getScopeId());
        Assert.assertEquals(aScopeId, jobStartingException.getScopeId());
        Assert.assertEquals(aJobId, jobStartingException.getJobId());
        Assert.assertEquals(exceptionMessage, jobStartingException.getMessage());
        Assert.assertEquals(exceptionMessage, jobStartingException.getLocalizedMessage());
    }

    @Test
    public void testJobStoppingException() {
        String exceptionMessage = "The Job Execution " + aJobExecutionId + " of Job " + aJobId + " in scope " + aScopeId + " cannot be stopped.";

        // Without cause
        JobStoppingException jobStoppingException = new JobStoppingException(aScopeId, aJobId, aJobExecutionId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_EXECUTION_STOPPING, jobStoppingException.getCode());
        Assert.assertNull(jobStoppingException.getCause());
        Assert.assertEquals(aScopeId, jobStoppingException.getScopeId());
        Assert.assertEquals(aJobId, jobStoppingException.getJobId());
        Assert.assertEquals(aJobExecutionId, jobStoppingException.getJobExecutionId());
        Assert.assertEquals(exceptionMessage, jobStoppingException.getMessage());
        Assert.assertEquals(exceptionMessage, jobStoppingException.getLocalizedMessage());

        // With cause
        exceptionMessage = "The Job " + aJobId + " in scope " + aScopeId + " cannot be stopped.";

        jobStoppingException = new JobStoppingException(aCause, aScopeId, aJobId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_STOPPING, jobStoppingException.getCode());
        Assert.assertNotNull(jobStoppingException.getCause());
        Assert.assertEquals(aScopeId, jobStoppingException.getScopeId());
        Assert.assertEquals(aScopeId, jobStoppingException.getScopeId());
        Assert.assertEquals(aJobId, jobStoppingException.getJobId());
        Assert.assertEquals(exceptionMessage, jobStoppingException.getMessage());
        Assert.assertEquals(exceptionMessage, jobStoppingException.getLocalizedMessage());

        // With cause and jobExecutionId
        exceptionMessage = "The Job Execution " + aJobExecutionId + " of Job " + aJobId + " in scope " + aScopeId + " cannot be stopped.";

        jobStoppingException = new JobStoppingException(aCause, aScopeId, aJobId, aJobExecutionId);

        Assert.assertEquals(JobEngineErrorCodes.JOB_EXECUTION_STOPPING, jobStoppingException.getCode());
        Assert.assertNotNull(jobStoppingException.getCause());
        Assert.assertEquals(aScopeId, jobStoppingException.getScopeId());
        Assert.assertEquals(aScopeId, jobStoppingException.getScopeId());
        Assert.assertEquals(aJobId, jobStoppingException.getJobId());
        Assert.assertEquals(aJobExecutionId, jobStoppingException.getJobExecutionId());
        Assert.assertEquals(exceptionMessage, jobStoppingException.getMessage());
        Assert.assertEquals(exceptionMessage, jobStoppingException.getLocalizedMessage());
    }
}
