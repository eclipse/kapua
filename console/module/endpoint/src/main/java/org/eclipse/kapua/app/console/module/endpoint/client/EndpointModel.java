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
package org.eclipse.kapua.app.console.module.endpoint.client;

import java.io.Serializable;

public interface EndpointModel extends Serializable {

    String getSchema();

    void setSchema(String schema);

    String getDns();

    void setDns(String dns);

    Number getPort();

    void setPort(Number port);

    boolean getSecure();

    void setSecure(boolean secure);

    String getEndpointType();

    void setEndpointType(String endpointType);

}
