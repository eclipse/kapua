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
package org.eclipse.kapua.broker.core.plugin.metric;

public class SecurityMetrics {

    private SecurityMetrics() { }

    public static final String METRIC_MODULE_NAME = "security";

    public static final String METRIC_COMPONENT_LOGIN = "login";
    public static final String METRIC_COMPONENT_PUBLISH = "publish";
    public static final String METRIC_COMPONENT_SUBSCRIBE = "subscribe";

    public static final String METRIC_CLIENTS = "clients";
    public static final String METRIC_KAPUASYS = "kapuasys";
    public static final String METRIC_CONNECT = "connect";
    public static final String METRIC_CONNECTED = "connected";
    public static final String METRIC_DISCONNECT = "disconnect";
    public static final String METRIC_DISCONNECTED = "disconnected";
    public static final String METRIC_INTERNAL_CONNECTOR = "internal_connector";
    public static final String METRIC_SUCCESS = "success";
    public static final String METRIC_FAILURE = "failure";
    public static final String METRIC_FAILURE_PASSWORD = METRIC_FAILURE + "_password";
    public static final String METRIC_FAILURE_CLIENT_ID = METRIC_FAILURE + "_client_id";
    public static final String METRIC_NORMAL = "normal";
    public static final String METRIC_STEALING_LINK = "stealing_link";
    public static final String METRIC_ADMIN_STEALING_LINK = "admin_" + METRIC_STEALING_LINK;
    public static final String METRIC_REMOTE_STEALING_LINK = "remote_" + METRIC_STEALING_LINK;
    public static final String METRIC_ADD_CONNECTION = "add_connection";
    public static final String METRIC_USER = "user";
    public static final String METRIC_SHIRO = "shiro";
    public static final String METRIC_CHECK_ACCESS = "check_access";
    public static final String METRIC_FIND_DEVICE_CONNECTION = "find_device_connection";
    public static final String METRIC_UPDATE_DEVICE_CONNECTION = "update_device_connection";
    public static final String METRIC_LOGOUT = "logout";
    public static final String METRIC_SEND_LOGIN_UPDATE = "send_login_update";
    public static final String METRIC_REMOVE_CONNECTION = "remove_connection";
    public static final String METRIC_ALLOWED = "allowed";
    public static final String METRIC_NOT_ALLOWED = "not_" + METRIC_ALLOWED;
    public static final String METRIC_MESSAGES = "messages";
    public static final String METRIC_SIZE = "size";
    public static final String METRIC_BYTES = "bytes";

    public static final String METRIC_COUNT = "count";
    public static final String METRIC_TIME = "time";
    public static final String METRIC_S = "s";

}
