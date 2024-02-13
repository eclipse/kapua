/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MetricsAuthentication {

    public static final String SERVICE_AUTHENTICATION = "service_authentication";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    private static final String GET_ACCOUNT = "getAccount";
    private static final String REQUEST = "request";
    private Counter login;
    private Counter loginRequest;
    private Counter logout;
    private Counter logoutRequest;
    private Counter getAccount;
    private Counter getAccountRequest;

    public static final String CONVERTER = "converter";
    private Counter converter;
    private Counter converterError;

    @Inject
    public MetricsAuthentication(MetricsService metricsService) {
        converter = metricsService.getCounter(SERVICE_AUTHENTICATION, CONVERTER, MetricsLabel.SUCCESS);
        converterError = metricsService.getCounter(SERVICE_AUTHENTICATION, CONVERTER, MetricsLabel.ERROR);

        login = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGIN, MetricsLabel.SUCCESS);
        loginRequest = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGIN, REQUEST);
        logout = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGOUT, MetricsLabel.SUCCESS);
        logoutRequest = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGOUT, REQUEST);
        getAccount = metricsService.getCounter(SERVICE_AUTHENTICATION, GET_ACCOUNT, MetricsLabel.SUCCESS);
        getAccountRequest = metricsService.getCounter(SERVICE_AUTHENTICATION, GET_ACCOUNT, REQUEST);
    }

    public Counter getConverter() {
        return converter;
    }

    public Counter getConverterError() {
        return converterError;
    }

    public Counter getLogin() {
        return login;
    }

    public Counter getLoginRequest() {
        return loginRequest;
    }

    public Counter getLogout() {
        return logout;
    }

    public Counter getLogoutRequest() {
        return logoutRequest;
    }

    public Counter getGetAccount() {
        return getAccount;
    }

    public Counter getGetAccountRequest() {
        return getAccountRequest;
    }

}
