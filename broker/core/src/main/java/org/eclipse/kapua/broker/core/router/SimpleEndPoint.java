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

import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.Exchange;
import org.eclipse.kapua.broker.core.message.MessageConstants;

@XmlRootElement(name = "simpleEndPoint")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "regex",
        "endPoint"
})
public class SimpleEndPoint implements EndPoint {

    @XmlTransient
    private Pattern pattern;
    @XmlTransient
    private String regexPlaceholderReplaced;

    private String regex;
    private String endPoint;

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
        return endPoint;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
        regexPlaceholderReplaced = EndPoint.replacePlaceholder(regex);
        pattern = EndPoint.parseRegex(regexPlaceholderReplaced);
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append("Regex: ");
        buffer.append(regexPlaceholderReplaced);
        buffer.append("\n");
        buffer.append(prefix);
        buffer.append("\t");
        buffer.append("End point: ");
        buffer.append(endPoint);
    }

}