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
package org.eclipse.kapua.broker.core.routeloader;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.eclipse.kapua.broker.core.router.PlaceholderReplacer;

@XmlRootElement(name = "endpointUriTo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "uri"
})
/**
 * Endpoint uri implementation
 *
 */
public class EndpointUriTo implements Endpoint {

    /**
     * Id
     */
    private String id;
    /**
     * Uri to invoke
     */
    private String uri;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException {
        processorDefinition.to(PlaceholderReplacer.replacePlaceholder(uri, ac));
    }

    @Override
    public org.apache.camel.Endpoint asEndpoint(CamelContext camelContext, Map<String, Object> ac) {
        throw new UnsupportedOperationException(String.format("Operation not allowed for the %s", this.getClass()));
    }

    @Override
    public String asUriEndpoint(CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException {
        return PlaceholderReplacer.replacePlaceholder(uri, ac);
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);
        buffer.append("EndpointUri - id: ");
        buffer.append(id);
        buffer.append(" uri: ");
        buffer.append(uri);
    }

}
