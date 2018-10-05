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
package org.eclipse.kapua.connector;

/**
 * Platform header and metric properties
 *
 */
public class Properties {

    private Properties() {
    }

    public static final String MESSAGE_CONNECTION_ID = new String("connection-id");
    public static final String MESSAGE_ORIGINAL_DESTINATION = new String("message-destination");
    public static final String MESSAGE_QOS = new String("message-qos");
    public static final String MESSAGE_TYPE = new String("message-type");
    public static final String MESSAGE_CONTENT = new String("message-content");
    public static final String MESSAGE_ENQUEUED_TIMESTAMP = "enqueuedTimestamp";
    public static final String MESSAGE_BROKER_ID = "brokerId";
    public static final String MESSAGE_CLIENT_ID = "clientId";
    public static final String MESSAGE_DEVICE_ADAPTER = "device-adapter";
    public static final String MESSAGE_SCOPE_ID = "scopeId";
    public static final String MESSAGE_SCOPE_NAME = "scopeName";

    // used by login update info asynch
    public static final String METRIC_USERNAME = "username";
    public static final String METRIC_ACCOUNT = "account";
    public static final String METRIC_CLIENT_ID = "clientId";
    public static final String METRIC_IP = "ip";

    // used by login update info asynch
    public static final String METRIC_USER_ID = "userId";
    public static final String METRIC_NODE_ID = "nodeId";

    public static final String HEADER_KAPUA_CONNECTION_ID = "KAPUA_CONNECTION_ID";
    public static final String HEADER_KAPUA_CLIENT_ID = "KAPUA_CLIENT_ID";
    public static final String HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL = "KAPUA_DEVICE_PROTOCOL";
    public static final String HEADER_KAPUA_SESSION = "KAPUA_SESSION";
    public static final String HEADER_KAPUA_BROKER_CONTEXT = "KAPUA_BROKER_CONTEXT";
    public static final String HEADER_KAPUA_PROCESSING_EXCEPTION = "KAPUA_PROCESSING_EXCEPTION";

}