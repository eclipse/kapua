/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.bean;

public interface MessageConstants {

    //common
    public static final String HEADER_REQUESTER = "requester";
    public static final String HEADER_ACTION = "action";
    public static final String HEADER_REQUEST_ID = "request_id";
    public static final String HEADER_USERNAME = "username";
    public static final String HEADER_CLIENT_ID = "client_id";
    public static final String HEADER_CLIENT_IP = "client_ip";
    public static final String HEADER_CONNECTION_ID = "connection_id";

    //response
    public static final String HEADER_RESULT_CODE = "result_code";
    public static final String HEADER_ERROR_CODE = "error_code";

}
