/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * {@link EndpointInfo} entity definition.<br>
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "endpointInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = EndpointInfoXmlRegistry.class, factoryMethod = "newEntity")
public interface EndpointInfo extends KapuaUpdatableEntity {

    String TYPE = "endpointInfo";

    String ENDPOINT_TYPE_RESOURCE = "resource";
    String ENDPOINT_TYPE_CORS = "cors";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlElement(name = "schema")
    String getSchema();

    void setSchema(String schema);

    @XmlElement(name = "dns")
    String getDns();

    void setDns(String dns);

    @XmlElement(name = "port")
    int getPort();

    void setPort(int port);

    @XmlElement(name = "secure")
    boolean getSecure();

    void setSecure(boolean secure);

    @XmlElement(name = "usages")
    <E extends EndpointUsage> Set<E> getUsages();

    void setUsages(Set<EndpointUsage> endpointUsages);

    default String toStringURI() {
        try {
            return new URI(
                    getSchema(),
                    null,
                    getDns(),
                    getPort(),
                    null,
                    null,
                    null
            ).toString();
        } catch (URISyntaxException e) {
            throw KapuaRuntimeException.internalError(e, "Unable to build URI for EndpointInfo: " + getId().toCompactId());
        }
    }

    String getEndpointType();

    void setEndpointType(String endpointType);
}
