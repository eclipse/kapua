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
import org.apache.camel.model.PipelineDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.router.PlaceholderReplacer;
import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlRootElement(name = "choiceRoot")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "choiceList",
        "otherwise"
})
/**
 * Choice brick root object.</br>
 * It's the container for the when/otherwise conditions
 *
 */
public class ChoiceRoot implements Brick {

    /**
     * Id
     */
    private String id;
    /**
     * Choice list (when conditions)
     */
    private List<Brick> choiceList;
    /**
     * Otherwise condition (fired if none of the previous step is fired)
     */
    private Brick otherwise;

    public ChoiceRoot() {
        choiceList = new ArrayList<>();
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElementWrapper(name = "choiceList")
    @XmlAnyElement(lax = true)
    public List<Brick> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<Brick> choiceList) {
        this.choiceList = choiceList;
    }

    @XmlPath("otherwise")
    @XmlAnyElement(lax = true)
    public Brick getOtherwise() {
        return otherwise;
    }

    public void setOtherwise(Brick otherwise) {
        this.otherwise = otherwise;
    }

    @Override
    public void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        ChoiceDefinition cd = null;
        if (processorDefinition instanceof RouteDefinition) {
            cd = ((RouteDefinition) processorDefinition).choice();
        } else if (processorDefinition instanceof ChoiceDefinition) {
            cd = ((ChoiceDefinition) processorDefinition).choice();
        } else if (processorDefinition instanceof PipelineDefinition) {
            cd = ((PipelineDefinition) processorDefinition).choice();
        }
        else {
            throw new UnsupportedOperationException(String.format("Unsupported ProcessDefinition [%s]... Only ChoiceDefinition, PipelineDefinition and RouteDefinition are allowed", this.getClass()));
        }
        appendRouteDefinitionInternal(cd, camelContext, ac);
    }

    private void appendRouteDefinitionInternal(ChoiceDefinition cd, CamelContext camelContext, Map<String, Object> ac) throws UnsupportedOperationException, KapuaException {
        cd.setId(PlaceholderReplacer.replacePlaceholder(id, ac));
        for (Brick choiceWhen : choiceList) {
            choiceWhen.appendBrickDefinition(cd, camelContext, ac);
        }
        cd.endChoice();
        if (otherwise != null) {
            cd.otherwise();
            otherwise.appendBrickDefinition(cd, camelContext, ac);
        }
        cd.end();
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);
        buffer.append("StepChoice - id: ");
        buffer.append(id);
        buffer.append("\n");
        prefix += "\t";
        for (Brick choiceWhen : choiceList) {
            buffer.append(prefix);
            choiceWhen.toLog(buffer, prefix);
            buffer.append("\n");
        }
        if (otherwise != null) {
            buffer.append(prefix);
            buffer.append("Otherwise");
            buffer.append("\n");
            otherwise.toLog(buffer, prefix + " \t");
        }
    }

}
