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
package org.eclipse.kapua.message;

import org.eclipse.kapua.message.xml.MessageXmlRegistry;
import org.eclipse.kapua.message.xml.MetricsXmlAdapter;
import org.eclipse.kapua.model.xml.BinaryXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * Kapua message payload object definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "payload")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "metrics", //
        "body" //
}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newPayload")
public interface KapuaPayload extends Payload {

    /**
     * Get the metrics map
     *
     * @return
     */
    @XmlElement(name = "metrics")
    @XmlJavaTypeAdapter(MetricsXmlAdapter.class)
    public Map<String, Object> getMetrics();

    /**
     * Set the metrics map
     *
     * @param metrics
     */
    public void setMetrics(Map<String, Object> metrics);

    /**
     * Get the message body
     *
     * @return
     */
    @XmlElement(name = "body")
    @XmlJavaTypeAdapter(BinaryXmlAdapter.class)
    public byte[] getBody();

    /**
     * Set the message body
     *
     * @param body
     */
    public void setBody(byte[] body);

    /**
     * Convert the message to a displayable String
     *
     * @return
     */
    public String toDisplayString();
}
