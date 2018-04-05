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
package org.eclipse.kapua.broker.core.route;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.RedeliveryPolicyDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "onException")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "exception",
        "maximumRedeliveries",
        "logRetryAttempted",
        "retryAttemptedLogLevel",
        "endpointList"
})
public class BasicOnException implements OnException {

    private final static Logger logger = LoggerFactory.getLogger(BasicOnException.class);

    private String id;
    private String exception;
    private String maximumRedeliveries;
    private String logRetryAttempted;
    private String retryAttemptedLogLevel;
    private List<Endpoint> endpointList;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @XmlAttribute
    public String getMaximumRedeliveries() {
        return maximumRedeliveries;
    }

    public void setMaximumRedeliveries(String maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
    }

    @XmlAttribute
    public String isLogRetryAttempted() {
        return logRetryAttempted;
    }

    public void setLogRetryAttempted(String logRetryAttempted) {
        this.logRetryAttempted = logRetryAttempted;
    }

    @XmlAttribute
    public String isRetryAttemptedLogLevel() {
        return retryAttemptedLogLevel;
    }

    public void setRetryAttemptedLogLevel(String retryAttemptedLogLevel) {
        this.retryAttemptedLogLevel = retryAttemptedLogLevel;
    }

    @XmlElementWrapper(name = "endpointList")
    @XmlAnyElement(lax = true)
    public List<Endpoint> getEndpointList() {
        return endpointList;
    }

    public void setEndpointList(List<Endpoint> endpointList) {
        this.endpointList = endpointList;
    }

    @Override
    public void appendExceptionDefinition(RouteDefinition routeDefinition, CamelContext camelContext) {
        OnExceptionDefinition exceptionDefinition = routeDefinition.onException(convertException());
        RedeliveryPolicyDefinition rpd = new RedeliveryPolicyDefinition();
        rpd.setMaximumRedeliveries(maximumRedeliveries);
        rpd.setLogRetryAttempted(logRetryAttempted);
        rpd.setRetryAttemptedLogLevel(LoggingLevel.valueOf(retryAttemptedLogLevel));
        exceptionDefinition.setRedeliveryPolicy(rpd);
        for (Endpoint endpoint : endpointList) {
            try {
                org.apache.camel.Endpoint ep = endpoint.asEndpoint(camelContext);
                exceptionDefinition.to(ep);
            } catch (UnsupportedOperationException e) {
                logger.info("Cannot get {} as Endpoint. Try to get it as Uri", endpoint);
                exceptionDefinition.to(endpoint.asUriEndpoint(camelContext));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Class<Throwable> convertException() {
        try {
            Class exceptionClass = Class.forName(getException());
            if (Throwable.class.isAssignableFrom(exceptionClass)) {
                return (Class<Throwable>) exceptionClass;
            }
        } catch (ClassNotFoundException e) {
            logger.error("Cannot instantiate the class {}. Class not found!", getException(), e);
        }
        throw new UnsupportedOperationException(String.format("The class %s must be a throwable object", this.getClass()));
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);
        buffer.append("On exception - id: ");
        buffer.append(id);
        buffer.append(" exception: ");
        buffer.append(exception);
        buffer.append(" maximum redeliveries: ");
        buffer.append(maximumRedeliveries);
        buffer.append(" log retry attempted: ");
        buffer.append(logRetryAttempted);
        buffer.append(" retry attempted log level: ");
        buffer.append(retryAttemptedLogLevel);
        buffer.append("\n");
        prefix += "\t";
        for (Endpoint endpoint : endpointList) {
            buffer.append(prefix);
            endpoint.toLog(buffer, prefix);
            buffer.append("\n");
        }
    }

}
