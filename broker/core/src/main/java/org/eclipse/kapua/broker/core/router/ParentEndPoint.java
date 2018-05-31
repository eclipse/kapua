/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.router;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.Exchange;
import org.eclipse.kapua.broker.core.message.MessageConstants;

@XmlRootElement(name = "endPoint")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "regex",
        "endPoints"
})
public class ParentEndPoint implements EndPoint {

    @XmlTransient
    private Pattern pattern;
    @XmlTransient
    private String regexPlaceholderReplaced;

    private String regex;
    private List<EndPoint> endPoints;

    @Override
    @XmlTransient
    public boolean matches(Exchange exchange, Object value, String previous, Map<String, Object> properties) {
        if (previous == null) {
            String originaTopic = exchange.getIn().getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class);
            // TODO if pattern is null it is an error so it should be correct to throw an exception or leave the NullPointerException raised by the method?!
            return pattern.matcher(originaTopic).matches();
        } else {
            return false;
        }
    }

    @Override
    @XmlTransient
    public String getEndpoint(Exchange exchange, Object value, String previous, Map<String, Object> properties) {
        for (EndPoint endPoint : endPoints) {
            if (endPoint.matches(exchange, value, previous, properties)) {
                return endPoint.getEndpoint(exchange, value, previous, properties);
            }
        }
        return null;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
        regexPlaceholderReplaced = EndPoint.replacePlaceholder(regex);
        pattern = EndPoint.parseRegex(regexPlaceholderReplaced);
    }

    @XmlAnyElement
    public List<EndPoint> getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(List<EndPoint> endPoints) {
        this.endPoints = endPoints;
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);
        buffer.append("Regex: ");
        buffer.append(regexPlaceholderReplaced);
        buffer.append("\n");
        prefix += "\t";
        for (EndPoint endPoint : endPoints) {
            buffer.append(prefix);
            endPoint.toLog(buffer, prefix);
            buffer.append("\n");
        }
    }

}
