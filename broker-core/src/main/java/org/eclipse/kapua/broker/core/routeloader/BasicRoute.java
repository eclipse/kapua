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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ChoiceDefinition;
import org.apache.camel.model.MulticastDefinition;
import org.apache.camel.model.PipelineDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.router.PlaceholderReplacer;

@XmlRootElement(name = "basicRoute")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "autoStartup",
        "from",
        "multicast",
        "routeList",
        "onExceptionList"
})
/**
 * Basic route implementation
 *
 */
public class BasicRoute implements Route {

    /**
     * Id
     */
    private String id;
    /**
     * Autostartup
     */
    private boolean autoStartup;
    /**
     * From (uri)
     */
    private String from;
    /**
     * List of brick that compose the route
     */
    private List<Brick> routeList;
    /**
     * Multicast the bricks (so every brick will be executed in parallel)
     */
    private boolean multicast;
    /**
     * Exception handling list
     */
    private List<OnException> onExceptionList;

    public BasicRoute() {
        routeList = new ArrayList<>();
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public boolean isAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @XmlAttribute
    public boolean isMulticast() {
        return multicast;
    }

    public void setMulticast(boolean multicast) {
        this.multicast = multicast;
    }

    @XmlAttribute
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @XmlAnyElement(lax = true)
    @XmlElementWrapper(name = "routeList")
    public List<Brick> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Brick> routeList) {
        this.routeList = routeList;
    }

    @XmlAnyElement(lax = true)
    @XmlElementWrapper(name = "onExceptionList")
    public List<OnException> getOnExceptionList() {
        return onExceptionList;
    }

    public void setOnExceptionList(List<OnException> onExceptionList) {
        this.onExceptionList = onExceptionList;
    }

    @Override
    public void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        processorDefinition.setId(PlaceholderReplacer.replacePlaceholder(id, ac));
        if (processorDefinition instanceof RouteDefinition) {
            appendBrickDefinitionInternal((RouteDefinition) processorDefinition, camelContext, ac);
        } else if (processorDefinition instanceof ChoiceDefinition) {
            check();
            appendBrickDefinitionInternal((ChoiceDefinition) processorDefinition, camelContext, ac);
        } else if (processorDefinition instanceof PipelineDefinition) {
            check();
            appendBrickDefinitionInternal((PipelineDefinition) processorDefinition, camelContext, ac);
        } else if (processorDefinition instanceof MulticastDefinition) {
            check();
            appendBrickDefinitionInternal((MulticastDefinition) processorDefinition, camelContext, ac);
        } else {
            throw new UnsupportedOperationException(
                    String.format("Unsupported ProcessDefinition [%s]... Only ChoiceDefinition, PipelineDefinition, PipelineDefinition and RouteDefinition are allowed", this.getClass()));
        }
    }

    private void appendBrickDefinitionInternal(RouteDefinition routeDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        routeDefinition.setAutoStartup(Boolean.toString(autoStartup));
        if (multicast) {
            MulticastDefinition md = routeDefinition.multicast();
            appendBrickDefinitionInternal(md, camelContext, ac);
            md.end();
        }
        else {
            PipelineDefinition pd = routeDefinition.pipeline();
            appendBrickDefinitionInternal(pd, camelContext, ac);
            pd.end();
        }
        for (OnException onException : onExceptionList) {
            onException.appendExceptionDefinition(routeDefinition, camelContext, ac);
        }
    }


    private void appendBrickDefinitionInternal(PipelineDefinition pipelineDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        for (Brick route : routeList) {
            route.appendBrickDefinition(pipelineDefinition, camelContext, ac);
        }
    }

    private void appendBrickDefinitionInternal(MulticastDefinition multicastDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        for (Brick route : routeList) {
            PipelineDefinition pd = multicastDefinition.pipeline();
            route.appendBrickDefinition(pd, camelContext, ac);
            pd.end();
        }
    }

    private void appendBrickDefinitionInternal(ChoiceDefinition choiceDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        if (multicast) {
            MulticastDefinition md = choiceDefinition.multicast();
            appendBrickDefinitionInternal(md, camelContext, ac);
            md.end();
        } else {
            PipelineDefinition pd = choiceDefinition.pipeline();
            appendBrickDefinitionInternal(pd, camelContext, ac);
            pd.end();
        }
    }

    private void check() {
        if (!StringUtils.isEmpty(from) || onExceptionList != null && onExceptionList.size() > 0) {
            throw new UnsupportedOperationException(String.format("Operation not allowed for the %s. The subroute cannot have from and/or exception handling set", this.getClass()));
        }
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);  
        buffer.append("Route - id: ");
        buffer.append(id);
        buffer.append(" from: ");
        buffer.append(from);
        buffer.append(" multicast: ");
        buffer.append(multicast);
        buffer.append(" auto startup: ");
        buffer.append(autoStartup);
        buffer.append("\n");
        prefix += "\t";
        for (Brick route : routeList) {
            buffer.append(prefix);
            route.toLog(buffer, prefix);
            buffer.append("\n");
        }
        if (onExceptionList != null && onExceptionList.size() > 0) {
            for (OnException onException : onExceptionList) {
                buffer.append(prefix);
                onException.toLog(buffer, prefix);
                buffer.append("\n");
            }
        } else {
            buffer.append(prefix);
            buffer.append("NO configured exception handling");
            buffer.append("\n");
        }
    }

}
