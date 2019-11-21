/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.proxy;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue.ValueType;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.exception.CleanJobDataException;
import org.eclipse.kapua.job.engine.exception.JobCheckRunningException;
import org.eclipse.kapua.job.engine.exception.JobInvalidTargetException;
import org.eclipse.kapua.job.engine.exception.JobMissingStepException;
import org.eclipse.kapua.job.engine.exception.JobMissingTargetException;
import org.eclipse.kapua.job.engine.exception.JobNotRunningException;
import org.eclipse.kapua.job.engine.exception.JobResumingException;
import org.eclipse.kapua.job.engine.exception.JobStartingException;
import org.eclipse.kapua.job.engine.exception.JobStoppingException;
import org.eclipse.kapua.job.engine.proxy.setting.JobEngineHttpProxySetting;
import org.eclipse.kapua.job.engine.proxy.setting.JobEngineHttpProxySettingKey;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@KapuaProvider
public class JobEngineServiceHttpProxy implements JobEngineService {

    private final WebTarget webTarget;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobEngineServiceHttpProxy.class);
    private final KapuaIdFactory kapuaIdFactory = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class);

    public JobEngineServiceHttpProxy() {
        String jobEngineBaseAddress = JobEngineHttpProxySetting.getInstance().getString(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_HTTP_BASEADDRESS);
        if (Strings.isNullOrEmpty(jobEngineBaseAddress)) {
            String errorCause = "No HTTP base address set for Job Engine Service";
            LOGGER.error(errorCause);
            throw new IllegalArgumentException(errorCause);
        }
        LOGGER.debug("Job Engine Service HTTP base address: {}", jobEngineBaseAddress);
        webTarget = ClientBuilder.newClient().target(jobEngineBaseAddress);
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        AccessToken accessToken = KapuaSecurityUtils.getSession().getAccessToken();
        String accessTokenId = null;
        if (accessToken != null) {
            accessTokenId = accessToken.getTokenId();
        }

        Invocation.Builder builder = webTarget
                .path(String.format("/%s/%s/start", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE);

        if (StringUtils.isNotEmpty(accessTokenId)) {
            builder.header("Authorization", "Bearer " + accessTokenId);
        }

        Response response = builder.post(null);
        if (response.getStatus() != 204) {
            handleErrorResponse(response);
        }

    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        try {
            Response response = webTarget.path(String.format("/%s/%s/start-with-options", scopeId.toCompactId(), jobId.toCompactId()))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + accessToken)
                    .post(Entity.json(XmlUtil.marshalJson(jobStartOptions)));

            if (response.getStatus() != 204) {
                handleErrorResponse(response);
            }
        } catch (JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/%s/%s/is-running", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);

        if (response.getStatus() != 200) {
            handleErrorResponse(response);
        }
        // FIXME Response should be an actual class so that it can be correctly deserialized as a JSON Object
        return Boolean.parseBoolean(response.readEntity(String.class));
    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/%s/%s/stop", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);

        if (response.getStatus() != 204) {
            handleErrorResponse(response);
        }
    }

    @Override
    public void stopJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/%s/%s/executions/%s/stop", scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);

        if (response.getStatus() != 204) {
            handleErrorResponse(response);
        }
    }

    @Override
    public void resumeJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/%s/%s/executions/%s/resume", scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);

        if (response.getStatus() != 204) {
            handleErrorResponse(response);
        }
    }

    @Override
    public void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/%s/%s/clean-data", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);

        if (response.getStatus() != 204) {
            handleErrorResponse(response);
        }
    }

    private void handleErrorResponse(Response response) throws KapuaException {
        try {
            String responseBody = response.readEntity(String.class);
            // Not using JAXB here. I need a flexible object, so I cannot deserialize to a POJO.
            JsonObject error = Json.createReader(new StringReader(responseBody)).readObject();
            switch (error.getString("errorCode")) {
                case "CLEAN_JOB_DATA":
                    throw new CleanJobDataException(null, kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)));
                case "ENTITY_NOT_FOUND":
                    throw new KapuaEntityNotFoundException(error.getJsonArray("arguments").getString(0), error.getJsonArray("arguments").getString(1));
                case "JOB_CHECK_RUNNING":
                    throw new JobCheckRunningException(null, kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)));
                case "JOB_NOT_RUNNING":
                    throw new JobNotRunningException(kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)));
                case "JOB_RESUMING":
                    throw new JobResumingException(null, kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").get(2).toString()));
                case "JOB_STARTING":
                    throw new JobStartingException(null, kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)));
                case "JOB_STOPPING":
                    throw new JobStoppingException(null, kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").get(2).toString()));
                case "JOB_TARGET_INVALID":
                    Set<KapuaId> targetSublist = error.getJsonArray("arguments").getJsonArray(2).stream().filter(jsonValue -> jsonValue.getValueType() == ValueType.STRING).map(jsonValue -> kapuaIdFactory.newKapuaId(((JsonString)jsonValue).getString())).collect(Collectors.toSet());
                    throw new JobInvalidTargetException(kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)), targetSublist);
                case "ILLEGAL_ARGUMENT":
                    throw new KapuaIllegalArgumentException(error.getJsonArray("arguments").getString(0), error.getJsonArray("arguments").getString(1));
                case "MISSING_TARGETS":
                    throw new JobMissingTargetException(kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)));
                case "MISSING_STEPS":
                    throw new JobMissingStepException(kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(0)), kapuaIdFactory.newKapuaId(error.getJsonArray("arguments").getString(1)));
                case "SUBJECT_UNAUTHORIZED":
                    throw new SubjectUnauthorizedException(XmlUtil.unmarshalJson(error.getJsonArray("arguments").getString(0), Permission.class, null));
                case "UNAUTHENTICATED":
                    throw new KapuaUnauthenticatedException();
                default:
                    throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, error.getString("message"), error.getJsonArray("arguments"));
            }
        } catch (JAXBException | XMLStreamException | SAXException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }
}
