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
package org.eclipse.kapua.service.elasticsearch.client.rest.lowlevel;

import org.opensearch.client.Request;

/**
 * {@link DeviceStoreClientRequest} backed by the OpenSearch low-level REST client.
 *
 * @since 2.1.0
 */
class OpensearchDeviceStoreClientRequest implements DeviceStoreClientRequest {

    private final Request request;

    OpensearchDeviceStoreClientRequest(Request request) {
        this.request = request;
    }

    Request unwrap() {
        return request;
    }

    @Override
    public void setJsonEntity(String json) {
        request.setJsonEntity(json);
    }

    @Override
    public void addParameter(String name, String value) {
        request.addParameter(name, value);
    }
}
