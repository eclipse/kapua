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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.CamelContext;
import org.apache.camel.component.bean.BeanEndpoint;
import org.apache.camel.model.ProcessorDefinition;

@XmlRootElement(name = "endpointBeanTo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "beanName",
        "method"
})
public class EndpointBeanTo implements Endpoint {

    private String id;
    private String beanName;
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
    public void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext) throws UnsupportedOperationException {
        processorDefinition.to(asBeanEndpoint(camelContext));
    }

    @Override
    public org.apache.camel.Endpoint asEndpoint(CamelContext camelContext) {
        return asBeanEndpoint(camelContext);
    }

    @Override
    public String asUriEndpoint(CamelContext camelContext) throws UnsupportedOperationException {
        return String.format("bean:%s?method=%s", beanName, method);
    }

    private BeanEndpoint asBeanEndpoint(CamelContext camelContext) {
        BeanEndpoint beanEndpoint = new BeanEndpoint();
        beanEndpoint.setCamelContext(camelContext);
        beanEndpoint.setBeanName(beanName);
        beanEndpoint.setMethod(method);
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
