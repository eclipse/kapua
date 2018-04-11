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
import org.apache.camel.model.ProcessorDefinition;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.router.PlaceholderReplacer;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "choiceLeaf")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "condition",
        "choiceList",
        "otherwise"
})
/**
 * Choice leaf brick implementation
 *
 */
public class ChoiceLeaf implements Brick {

    private final static Logger logger = LoggerFactory.getLogger(ChoiceLeaf.class);

    /**
     * Id
     */
    private String id;
    /**
     * Condition to check
     */
    private String condition;
    /**
     * Steps list (to follow if the condition is satisfied)
     */
    private List<Brick> choiceList;
    /**
     * Otherwise step (if the condition is not satisfied)
     */
    private Brick otherwise;

    public ChoiceLeaf() {
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
        if (processorDefinition instanceof ChoiceDefinition) {
            ProcessorDefinition<ChoiceDefinition> whenChoiceDefinition = ((ChoiceDefinition) processorDefinition).when().simple(
                    PlaceholderReplacer.replacePlaceholder(condition, ac));
            whenChoiceDefinition.setId(PlaceholderReplacer.replacePlaceholder(id, ac));
            for (Brick choice : choiceList) {
                if (choice instanceof Endpoint) {
                    try {
                        org.apache.camel.Endpoint ep = ((Endpoint) choice).asEndpoint(camelContext, ac);
                        whenChoiceDefinition.to(ep);
                    } catch (UnsupportedOperationException e) {
                        logger.info("Cannot get {} as Endpoint. Try to get it as Uri", ((Endpoint) choice));
                        whenChoiceDefinition.to(((Endpoint) choice).asUriEndpoint(camelContext, ac));
                    }
                } else {
                    choice.appendBrickDefinition(((ChoiceDefinition) processorDefinition), camelContext, ac);
                }
            }
            ((ChoiceDefinition) processorDefinition).endChoice();
            whenChoiceDefinition.end();
            if (otherwise != null) {
                ((ChoiceDefinition) processorDefinition).otherwise();
                otherwise.appendBrickDefinition(((ChoiceDefinition) processorDefinition), camelContext, ac);
            }
        }
        else {
            throw new UnsupportedOperationException(String.format("Unsupported ProcessDefinition [%s]... Only ChoiceDefinition is allowed", this.getClass()));
        }
    }

    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append(prefix);
        buffer.append("StepChoiceWhen - id");
        buffer.append(id);
        buffer.append(" condition: ");
        buffer.append(condition);
        buffer.append("\n");
        prefix += "\t";
        for (Brick choice : choiceList) {
            buffer.append(prefix);
            choice.toLog(buffer, prefix);
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
