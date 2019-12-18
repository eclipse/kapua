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
import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
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
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@KapuaProvider
public class JobEngineServiceHttpProxy implements JobEngineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobEngineServiceHttpProxy.class);
    private final KapuaIdFactory kapuaIdFactory = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class);
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_KAPUA_SESSION = "X-Kapua-Session";
    private final String jobEngineBaseAddress;
    private final CloseableHttpClient client;
    private final char[] emptyCharArray = "".toCharArray();

    public JobEngineServiceHttpProxy() throws CertificateException, IOException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        JobEngineHttpProxySetting jobEngineHttpProxySetting = JobEngineHttpProxySetting.getInstance();
        String jobEngineBaseAddress = StringUtils.stripEnd(jobEngineHttpProxySetting.getString(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_HTTP_BASEADDRESS), "/");
        if (Strings.isNullOrEmpty(jobEngineBaseAddress)) {
            String errorCause = "No HTTP base address set for Job Engine Service";
            LOGGER.error(errorCause);
            throw new IllegalArgumentException(errorCause);
        }
        LOGGER.debug("Job Engine Service HTTP base address: {}", jobEngineBaseAddress);
        this.jobEngineBaseAddress = jobEngineBaseAddress;

        String trustStorePath = jobEngineHttpProxySetting.getString(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_TRUSTSTORE_PATH);
        String trustStorePassword = jobEngineHttpProxySetting.getString(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_TRUSTSTORE_PASSWORD);
        String keyStorePath = jobEngineHttpProxySetting.getString(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_KEYSTORE_PATH);
        String keyStorePassword = jobEngineHttpProxySetting.getString(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_KEYSTORE_PASSWORD);
        boolean verifyHostname = jobEngineHttpProxySetting.getBoolean(JobEngineHttpProxySettingKey.MICROSERVICE_JOBENGINE_VERIFY_HOSTNAME, true);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        SSLContextBuilder sslContextBuilder = SSLContexts.custom();

        if (StringUtils.isNotBlank(trustStorePath)) {
            KeyStore trustStore = KeyStore.getInstance("pkcs12");
            trustStore.load(new FileInputStream(trustStorePath), StringUtils.isNotEmpty(trustStorePassword) ? trustStorePassword.toCharArray() : null);
            sslContextBuilder.loadTrustMaterial(
                    trustStore,
                    null
            );
        }

        if (StringUtils.isNotBlank(keyStorePath)) {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            keyStore.load(new FileInputStream(keyStorePath), StringUtils.isNotEmpty(keyStorePassword) ? keyStorePassword.toCharArray() : null);
            sslContextBuilder
                    .loadKeyMaterial(
                    keyStore,
                    StringUtils.isNotEmpty(keyStorePassword) ? keyStorePassword.toCharArray() : null
            );
        }

        SSLContext sslContext = sslContextBuilder.build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{ "TLSv1.2" },
                null,
                verifyHostname ? SSLConnectionSocketFactory.getDefaultHostnameVerifier() : new NoopHostnameVerifier());

        client = httpClientBuilder
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/start", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId()));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/start-with-options", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId()));
            httpPost.setEntity(new StringEntity(XmlUtil.marshalJson(jobStartOptions), ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/is-running", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId()));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
                // FIXME Response should be an actual class so that it can be correctly deserialized as a JSON Object
                boolean isRunning = Boolean.parseBoolean(IOUtils.toString(httpResponse.getEntity().getContent()));
                EntityUtils.consume(httpResponse.getEntity());
                return isRunning;
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/stop", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId()));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public void stopJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/executions/%s/stop", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId()));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }

    }

    @Override
    public void resumeJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/executions/%s/resume", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId(), jobExecutionId.toCompactId()));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        try {
            HttpPost httpPost = buildPost(String.format("%s/%s/%s/clean-data", jobEngineBaseAddress, scopeId.toCompactId(), jobId.toCompactId()));
            try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                if (httpResponse.getStatusLine().getStatusCode() != 204) {
                    handleErrorResponse(httpResponse);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    private void handleErrorResponse(HttpResponse response) throws KapuaException {
        try {
            String responseBody = IOUtils.toString(response.getEntity().getContent());
            EntityUtils.consume(response.getEntity());
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
        } catch (IOException | JAXBException | XMLStreamException | SAXException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    private HttpPost buildPost(String uri) throws JAXBException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader(HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.addHeader(HEADER_KAPUA_SESSION, new String(Base64.getEncoder().encode(XmlUtil.marshalJson(KapuaSecurityUtils.getSession()).getBytes())));
        return httpPost;
    }

}
