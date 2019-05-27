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

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.proxy.setting.JobEngineHttpProxySetting;
import org.eclipse.kapua.job.engine.proxy.setting.JobEngineHttpProxySettingKey;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class JobEngineServiceHttpProxy implements JobEngineService {

    private final WebTarget webTarget;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobEngineServiceHttpProxy.class);

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
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/startJobWithOptions/%s/%s", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);
        System.out.println(response.getStatusInfo());
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        try {
            Response response = webTarget.path(String.format("/startJobWithOptions/%s/%s", scopeId.toCompactId(), jobId.toCompactId()))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + accessToken)
                    .post(Entity.text(XmlUtil.marshalJson(jobStartOptions)));
            System.out.println(response.getStatusInfo());
        } catch (JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        Response response = webTarget.path(String.format("/isRunning/%s/%s", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);
        // FIXME Response should be an actual class so that it can be correctly deserialized as a JSON Object
        return Boolean.parseBoolean(response.readEntity(String.class));
    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        webTarget.path(String.format("/stopJob/%s/%s", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);
    }

    @Override
    public void stopJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        webTarget.path(String.format("/stopJobExecution/%s/%s/%s", scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);
    }

    @Override
    public void resumeJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        webTarget.path(String.format("/resumeJobExecution/%s/%s/%s", scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);
    }

    @Override
    public void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        String accessToken = KapuaSecurityUtils.getSession().getAccessToken().getTokenId();

        webTarget.path(String.format("/cleanJobData/%s/%s", scopeId.toCompactId(), jobId.toCompactId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(null);
    }
}
