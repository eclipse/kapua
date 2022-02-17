/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.endpoint.shared.util;

import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.service.endpoint.EndpointInfo;

public class KapuaGwtEndpointModelConverter {

    private KapuaGwtEndpointModelConverter() {
    }

    /**
     * Converts a {@link EndpointInfo} into a {@link GwtEndpoint}
     *
     * @param endpoint The {@link EndpointInfo} to convertKapuaId
     * @return The converted {@link GwtEndpoint}
     * @since 1.0.0
     */
    public static GwtEndpoint convertEndpoint(EndpointInfo endpoint) {

        GwtEndpoint gwtEndpoint = new GwtEndpoint();
        //
        // Covert commons attributes
        KapuaGwtCommonsModelConverter.convertEntity(endpoint, gwtEndpoint);

        //
        // Convert other attributes
        gwtEndpoint.setSchema(endpoint.getSchema());
        gwtEndpoint.setDns(endpoint.getDns());
        gwtEndpoint.setPort(endpoint.getPort());
        gwtEndpoint.setSecure(endpoint.getSecure());
        gwtEndpoint.setEndpointType(endpoint.getEndpointType());

        return gwtEndpoint;
    }
}
