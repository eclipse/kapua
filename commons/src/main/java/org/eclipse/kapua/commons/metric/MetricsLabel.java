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
package org.eclipse.kapua.commons.metric;

public class MetricsLabel {

    public static final String MODULE_CONVERTER = "converter";
    public static final String MODULE_CORE = "core";
    public static final String MODULE_DATASTORE = "datastore";
    public static final String MODULE_SECURITY = "security";
    public static final String MODULE_BROKER = "broker";

    public static final String COMPONENT_DATASTORE = "datastore";
    public static final String COMPONENT_DRIVER = "driver";
    public static final String COMPONENT_EVENT = "event";
    public static final String COMPONENT_LOGIN = "login";
    public static final String COMPONENT_PUBLISH = "publish";
    public static final String COMPONENT_REST_CLIENT = "rest-client";
    public static final String COMPONENT_SUBSCRIBE = "subscribe";
    public static final String COMPONENT_PROCESSOR = "processor";
    public static final String COMPONENT_CORE = "core";

    public static final String COMPONENT_KAPUA = "kapua";

    public static final String CONNECTION = "connection";
    public static final String SESSION = "session";
    public static final String ACL = "acl";
    public static final String BROKER_CONNECTION = "broker_connection";
    public static final String SESSION_CONTEXT = "session_context";
    public static final String SESSION_CONTEXT_BY_CLIENT = "session_context_by_client";
    public static final String ACTIVE_CONNECTION = "active_connection";
    public static final String DISK_USAGE = "disk_usage";
    public static final String TOTAL_CONNECTION = "total_connection";
    public static final String TOTAL_MESSAGE = "total_message";
    public static final String TOTAL_MESSAGE_ACKNOWLEDGED = "total_message_acknowledged";
    public static final String TOTAL_MESSAGE_ADDED = "total_message_added";

    public static final String MESSAGE_FORMAT_JMS = "jms";
    public static final String MESSAGE_FORMAT_JSON = "Json";
    public static final String KAPUA_MESSAGE = "kapua_message";

    public static final String MESSAGE_LIFECYCLE = "lifecycle";
    public static final String MESSAGE_DATA = "data";
    public static final String MESSAGE_APPS = "apps";
    public static final String MESSAGE_BIRTH = "birth";
    public static final String MESSAGE_DC = "dc";
    public static final String MESSAGE_MISSING = "missing";
    public static final String MESSAGE_NOTIFY = "notify";
    public static final String MESSAGE_UNMATCHED = "unmatched";

    public static final String TO = "to";
    public static final String MESSAGES = "messages";
    public static final String ATTEMPT = "attempt";
    public static final String LOG = "log";

    //action
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String CONNECT = "connect";
    public static final String DISCONNECT = "disconnect";
    public static final String REQUEST = "request";
    public static final String STORE = "store";
    public static final String ENQUEUED_EVENT = "enqueued_event";
    public static final String DEQUEUED_EVENT = "dequeued_event";
    public static final String PROCESSED_EVENT = "processed_event";

    public static final String OK = "ok";
    public static final String ERROR = "error";
    public static final String FAILURE = "failure";
    public static final String SUCCESS = "success";
    public static final String UNAUTHENTICATED = "unauthenticated";
    public static final String ALLOWED = "allowed";
    public static final String NOT_ALLOWED = "not_allowed";

    public static final String PROCESS_QUEUE = "process_queue";
    public static final String COMMUNICATION = "communication";
    public static final String CONFIGURATION = "configuration";
    public static final String VALIDATION = "validation";
    public static final String GENERIC = "generic";

    public static final String COUNT = "count";
    public static final String GAUGE = "gauge";
    public static final String QUEUE = "queue";
    public static final String LAST = "last";
    public static final String TIME = "time";
    public static final String SECONDS = "s";
    public static final String MILLI_SECONDS = "ms";
    public static final String SIZE = "size";
    public static final String BYTES = "bytes";
    public static final String TOTAL = "total";


    public static final String EXEC_TIME = "exec_time";
    public static final String RUNTIME_EXEC = "runtime_exec";
    public static final String TIMEOUT_RETRY = "timeout_retry";
    public static final String TIMEOUT_RETRY_LIMIT_REACHED = "timeout_retry_limit_reached";

    //datastore
    public static final String ALREADY_IN_THE_STORE = "already_in_the_store";

    private MetricsLabel() {
    }

}
