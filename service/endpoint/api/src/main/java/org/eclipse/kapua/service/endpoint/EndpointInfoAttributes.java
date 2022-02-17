/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.endpoint;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

public class EndpointInfoAttributes extends KapuaUpdatableEntityAttributes {

    public static final String SCHEMA = "schema";
    public static final String DNS = "dns";
    public static final String PORT = "port";
    public static final String SECURE = "secure";
    public static final String USAGES = "usages";
    public static final String ENDPOINT_TYPE = "endpointType";

}
