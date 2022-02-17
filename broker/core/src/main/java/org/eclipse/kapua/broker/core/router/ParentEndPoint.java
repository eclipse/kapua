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
        return EndpointsUtil.matches(exchange, value, previous, properties, pattern);
    }

    @Override
    @XmlTransient
    public String getEndPoint(Exchange exchange, Object value, String previous, Map<String, Object> properties) {
        for (EndPoint endPoint : endPoints) {
            if (endPoint.matches(exchange, value, previous, properties)) {
                return endPoint.getEndPoint(exchange, value, previous, properties);
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
