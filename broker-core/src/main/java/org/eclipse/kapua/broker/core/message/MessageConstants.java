/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.message;

/**
 * Kapua message constants (metrics name and header keys)
 *
 * @since 1.0
 */
public class MessageConstants {

    private MessageConstants() {
    }

    // used by login update info asynch
    public static final String METRIC_USERNAME = "username";
    public static final String METRIC_ACCOUNT = "account";
    public static final String METRIC_CLIENT_ID = "clientId";
    public static final String METRIC_IP = "ip";
    public final static String PROPERTY_ORIGINAL_TOPIC = "originalTopic";
    public final static String PROPERTY_ENQUEUED_TIMESTAMP = "enqueuedTimestamp";
    public static final String PROPERTY_BROKER_ID = "brokerId";
    public static final String PROPERTY_CLIENT_ID = "clientId";
    public static final String PROPERTY_SCOPE_ID = "scopeId";

    // used by login update info asynch
    public static final String METRIC_USER_ID = "userId";
    public static final String METRIC_NODE_ID = "nodeId";

    public static final String HEADER_KAPUA_CONNECTION_ID = "KAPUA_CONNECTION_ID";
    public static final String HEADER_KAPUA_CLIENT_ID = "KAPUA_CLIENT_ID";
    public static final String HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL = "KAPUA_DEVICE_PROTOCOL";
    public static final String HEADER_KAPUA_SESSION = "KAPUA_SESSION";
    public static final String HEADER_KAPUA_BROKER_CONTEXT = "KAPUA_BROKER_CONTEXT";

}
