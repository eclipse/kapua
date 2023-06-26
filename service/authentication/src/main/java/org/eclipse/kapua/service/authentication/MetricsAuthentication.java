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

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;

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

    public static final String CONVERSION = "conversion";
    private Counter authentication;
    private Counter authenticationError;

    private static MetricsAuthentication instance;

    public static MetricsAuthentication getInstance() {
        if (instance == null) {
            synchronized (CommonsMetric.class) {
                if (instance == null) {
                    instance = new MetricsAuthentication();
                }
            }
        }
        return instance;
    }

    private MetricsAuthentication() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        //authentication
        authentication = metricsService.getCounter(SERVICE_AUTHENTICATION, CONVERSION, MetricsLabel.SUCCESS);
        authenticationError = metricsService.getCounter(SERVICE_AUTHENTICATION, CONVERSION, MetricsLabel.ERROR);

        login = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGIN, MetricsLabel.SUCCESS);
        loginRequest = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGIN, REQUEST);
        logout = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGOUT, MetricsLabel.SUCCESS);
        logoutRequest = metricsService.getCounter(SERVICE_AUTHENTICATION, LOGOUT, REQUEST);
        getAccount = metricsService.getCounter(SERVICE_AUTHENTICATION, GET_ACCOUNT, MetricsLabel.SUCCESS);
        getAccountRequest = metricsService.getCounter(SERVICE_AUTHENTICATION, GET_ACCOUNT, REQUEST);
    }

    public Counter getAuthentication() {
        return authentication;
    }

    public Counter getAuthenticationError() {
        return authenticationError;
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
