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
import org.apache.camel.component.bean.BeanEndpoint;
import org.apache.camel.model.ProcessorDefinition;
import org.eclipse.kapua.broker.core.router.PlaceholderReplacer;

@XmlRootElement(name = "endpointBeanTo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "beanName",
        "method"
})
/**
 * Endpoint bean implementation
 *
 */
public class EndpointBeanTo implements Endpoint {

    /**
     * Id (it's the step Id not the spring bean id)
     */
    private String id;
    /**
     * Bean name
     */
    private String beanName;
    /**
     * Method to invoke
     */
    private String method;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @XmlAttribute
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException {
        processorDefinition.to(asBeanEndpoint(camelContext, ac));
    }

    @Override
    public org.apache.camel.Endpoint asEndpoint(CamelContext camelContext, Map<String, Object> ac) {
        return asBeanEndpoint(camelContext, ac);
    }

    @Override
    public String asUriEndpoint(CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException {
        return PlaceholderReplacer.replacePlaceholder(String.format("bean:%s?method=%s", beanName, method), ac);
    }

    private BeanEndpoint asBeanEndpoint(CamelContext camelContext, Map<String, Object> ac) {
        BeanEndpoint beanEndpoint = new BeanEndpoint();
        beanEndpoint.setCamelContext(camelContext);
        beanEndpoint.setBeanName(PlaceholderReplacer.replacePlaceholder(beanName, ac));
        beanEndpoint.setMethod(PlaceholderReplacer.replacePlaceholder(method, ac));
        return beanEndpoint;
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);
        buffer.append("EndpointBeanTo - id:");
        buffer.append(id);
        buffer.append(" beanName: ");
        buffer.append(beanName);
        buffer.append(" method: ");
        buffer.append(method);
    }

}
