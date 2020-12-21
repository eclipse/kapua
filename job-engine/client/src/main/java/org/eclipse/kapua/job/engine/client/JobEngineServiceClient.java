/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningResponse;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.client.settings.JobEngineClientSetting;
import org.eclipse.kapua.job.engine.client.settings.JobEngineClientSettingKeys;
import org.eclipse.kapua.job.engine.exception.CleanJobDataException;
import org.eclipse.kapua.job.engine.exception.JobAlreadyRunningException;
import org.eclipse.kapua.job.engine.exception.JobExecutionEnqueuedException;
import org.eclipse.kapua.job.engine.exception.JobInvalidTargetException;
import org.eclipse.kapua.job.engine.exception.JobMissingStepException;
import org.eclipse.kapua.job.engine.exception.JobMissingTargetException;
import org.eclipse.kapua.job.engine.exception.JobNotRunningException;
import org.eclipse.kapua.job.engine.exception.JobResumingException;
import org.eclipse.kapua.job.engine.exception.JobRunningException;
import org.eclipse.kapua.job.engine.exception.JobStartingException;
import org.eclipse.kapua.job.engine.exception.JobStoppingException;
import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobExecutionEnqueuedExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;

import org.eclipse.kapua.app.api.core.exception.model.EntityNotFoundExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@KapuaProvider
public class JobEngineServiceClient implements JobEngineService {

    private final WebTarget jobEngineTarget;
    private final Logger log = LoggerFactory.getLogger(JobEngineServiceClient.class);

    public JobEngineServiceClient() {
        Client jobEngineClient = ClientBuilder.newClient().register(SessionInfoFilter.class).register(MoxyJsonFeature.class);
        jobEngineTarget = jobEngineClient.target(JobEngineClientSetting.getInstance().getString(JobEngineClientSettingKeys.JOB_ENGINE_BASE_URL));
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            String path = String.format("start/%s/%s", scopeId.toCompactId(), jobId.toCompactId());
            log.debug("JobEngine POST Call to {}", path);
            Response response = jobEngineTarget.path(String.format("start/%s/%s", scopeId.toCompactId(), jobId.toCompactId()))
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .post(null);
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine POST Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
        } catch (ClientErrorException clientErrorException) {
            throw KapuaException.internalError(clientErrorException);
        }
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) throws KapuaException {
        try {
            String path = String.format("start-with-options/%s/%s", scopeId.toCompactId(), jobId.toCompactId());
            String jobStartOptionsJson = XmlUtil.marshalJson(jobStartOptions);
            log.debug("JobEngine POST Call to {}, body {}", path, jobStartOptionsJson);
            Response response = jobEngineTarget.path(path)
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .post(Entity.json(jobStartOptionsJson));
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine POST Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
        } catch (ClientErrorException | JAXBException e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            String path = String.format("is-running/%s/%s", scopeId.toCompactId(), jobId.toCompactId());
            log.debug("JobEngine GET Call to {}", path);
            Response response = jobEngineTarget.path(path)
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .get();
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine GET Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
            IsJobRunningResponse isRunningJobResponse = XmlUtil.unmarshalJson(responseText, IsJobRunningResponse.class, null);
            return isRunningJobResponse.isRunning();
        } catch (ClientErrorException | JAXBException | SAXException | XMLStreamException e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            String path = String.format("stop/%s/%s", scopeId.toCompactId(), jobId.toCompactId());
            log.debug("JobEngine POST Call to {}", path);
            Response response = jobEngineTarget.path(path)
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .post(null);
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine POST Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
        } catch (ClientErrorException clientErrorException) {
            throw KapuaException.internalError(clientErrorException);
        }
    }

    @Override
    public void stopJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        try {
            String path = String.format("stop-execution/%s/%s/%s", scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId());
            log.debug("JobEngine POST Call to {}", path);
            Response response = jobEngineTarget.path(path)
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .post(null);
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine POST Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
        } catch (ClientErrorException clientErrorException) {
            throw KapuaException.internalError(clientErrorException);
        }
    }

    @Override
    public void resumeJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        try {
            String path = String.format("resume-execution/%s/%s/%s", scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId());
            log.debug("JobEngine POST Call to {}", path);
            Response response = jobEngineTarget.path(path)
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .post(null);
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine POST Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
        } catch (ClientErrorException clientErrorException) {
            throw KapuaException.internalError(clientErrorException);
        }
    }

    @Override
    public void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            String path = String.format("clean-data/%s/%s", scopeId.toCompactId(), jobId.toCompactId());
            log.debug("JobEngine POST Call to {}", path);
            Response response = jobEngineTarget.path(path)
                                               .request(MediaType.APPLICATION_JSON_TYPE)
                                               .accept(MediaType.APPLICATION_JSON_TYPE)
                                               .post(null);
            String responseText = response.readEntity(String.class);
            log.debug("JobEngine POST Call to {} - response code {} - body {}", path, response.getStatus(), responseText);
            Family family = response.getStatusInfo().getFamily();
            if (family == Family.CLIENT_ERROR || family == Family.SERVER_ERROR) {
                handleJobEngineException(responseText, response.getStatus());
            }
        } catch (ClientErrorException clientErrorException) {
            throw KapuaException.internalError(clientErrorException);
        }
    }

    private void handleJobEngineException(String responseText, int statusCode) throws KapuaException {
        try {
            log.error("Job Engine REST Error: {} - status code {}", responseText, statusCode);
            if (StringUtils.isBlank(responseText)) {
                throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, "Job Engine returned an error but no message was given");
            }
            ExceptionInfo exceptionInfo = XmlUtil.unmarshalJson(responseText, ExceptionInfo.class, null);
            switch (exceptionInfo.getKapuaErrorCode()) {
                case "ENTITY_NOT_FOUND":
                    EntityNotFoundExceptionInfo entityNotFoundExceptionInfo = XmlUtil.unmarshalJson(responseText, EntityNotFoundExceptionInfo.class, null);
                    throw new KapuaEntityNotFoundException(entityNotFoundExceptionInfo.getEntityType(), entityNotFoundExceptionInfo.getEntityId());
                case "CANNOT_CLEANUP_JOB_DATA":
                    CleanJobDataExceptionInfo cleanJobDataExceptionInfo = XmlUtil.unmarshalJson(responseText, CleanJobDataExceptionInfo.class, null);
                    throw new CleanJobDataException(cleanJobDataExceptionInfo.getScopeId(), cleanJobDataExceptionInfo.getJobId());
                case "JOB_ALREADY_RUNNING":
                    JobAlreadyRunningExceptionInfo jobAlreadyRunningExceptionInfo = XmlUtil.unmarshalJson(responseText, JobAlreadyRunningExceptionInfo.class, null);
                    throw new JobAlreadyRunningException(jobAlreadyRunningExceptionInfo.getScopeId(),
                                                         jobAlreadyRunningExceptionInfo.getJobId(),
                                                         jobAlreadyRunningExceptionInfo.getExecutionId(),
                                                         jobAlreadyRunningExceptionInfo.getJobTargetIdSubset());
                case "JOB_EXECUTION_ENQUEUED":
                    JobExecutionEnqueuedExceptionInfo jobExecutionEnqueuedExceptionInfo = XmlUtil.unmarshalJson(responseText, JobExecutionEnqueuedExceptionInfo.class, null);
                    throw new JobExecutionEnqueuedException(jobExecutionEnqueuedExceptionInfo.getScopeId(),
                                                            jobExecutionEnqueuedExceptionInfo.getJobId(),
                                                            jobExecutionEnqueuedExceptionInfo.getExecutionId(),
                                                            jobExecutionEnqueuedExceptionInfo.getEnqueuedJobExecutionId());
                case "JOB_TARGET_INVALID":
                    JobInvalidTargetExceptionInfo jobInvalidTargetExceptionInfo = XmlUtil.unmarshalJson(responseText, JobInvalidTargetExceptionInfo.class, null);
                    throw new JobInvalidTargetException(jobInvalidTargetExceptionInfo.getScopeId(), jobInvalidTargetExceptionInfo.getJobId(), jobInvalidTargetExceptionInfo.getJobTargetIdSubset());
                case "JOB_STEP_MISSING":
                    JobMissingStepExceptionInfo jobMissingStepExceptionInfo = XmlUtil.unmarshalJson(responseText, JobMissingStepExceptionInfo.class, null);
                    throw new JobMissingStepException(jobMissingStepExceptionInfo.getScopeId(), jobMissingStepExceptionInfo.getJobId());
                case "JOB_TARGET_MISSING":
                    JobMissingTargetExceptionInfo jobMissingTargetExceptionInfo = XmlUtil.unmarshalJson(responseText, JobMissingTargetExceptionInfo.class, null);
                    throw new JobMissingTargetException(jobMissingTargetExceptionInfo.getScopeId(), jobMissingTargetExceptionInfo.getJobId());
                case "JOB_NOT_RUNNING":
                    JobNotRunningExceptionInfo jobNotRunningExceptionInfo = XmlUtil.unmarshalJson(responseText, JobNotRunningExceptionInfo.class, null);
                    throw new JobNotRunningException(jobNotRunningExceptionInfo.getScopeId(), jobNotRunningExceptionInfo.getJobId());
                case "JOB_RESUMING":
                    JobResumingExceptionInfo jobResumingExceptionInfo = XmlUtil.unmarshalJson(responseText, JobResumingExceptionInfo.class, null);
                    throw new JobResumingException(jobResumingExceptionInfo.getScopeId(), jobResumingExceptionInfo.getJobId(), jobResumingExceptionInfo.getExecutionId());
                case "JOB_RUNNING":
                    JobRunningExceptionInfo jobRunningExceptionInfo = XmlUtil.unmarshalJson(responseText, JobRunningExceptionInfo.class, null);
                    throw new JobRunningException(jobRunningExceptionInfo.getScopeId(), jobRunningExceptionInfo.getJobId());
                case "JOB_STARTING":
                    JobStartingExceptionInfo jobStartingExceptionInfo = XmlUtil.unmarshalJson(responseText, JobStartingExceptionInfo.class, null);
                    throw new JobStartingException(jobStartingExceptionInfo.getScopeId(), jobStartingExceptionInfo.getJobId());
                case "JOB_STOPPING":
                    JobStoppingExceptionInfo jobStoppingExceptionInfo = XmlUtil.unmarshalJson(responseText, JobStoppingExceptionInfo.class, null);
                    throw new JobStoppingException(jobStoppingExceptionInfo.getScopeId(), jobStoppingExceptionInfo.getJobId(),jobStoppingExceptionInfo.getExecutionId());
                default:
                    throw KapuaException.internalError(exceptionInfo.getMessage());
            }
        } catch (JAXBException | SAXException | XMLStreamException e) {
            throw KapuaException.internalError(e);
        }
    }

}
