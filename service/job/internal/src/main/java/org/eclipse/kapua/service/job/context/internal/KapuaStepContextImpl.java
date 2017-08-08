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
package org.eclipse.kapua.service.job.context.internal;

import java.io.Serializable;
import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import javax.batch.runtime.context.StepContext;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.context.StepContextPropertyNames;
import org.xml.sax.SAXException;

public class KapuaStepContextImpl implements KapuaStepContext {

    private StepContext stepContext;

    public KapuaStepContextImpl(StepContext stepContext) {
        this.stepContext = stepContext;
    }

    public int getStepIndex() {
        Properties jobContextProperties = stepContext.getProperties();
        String stepIndexString = jobContextProperties.getProperty(StepContextPropertyNames.STEP_INDEX);
        return stepIndexString != null ? Integer.parseInt(stepIndexString) : null;
    }

    public Integer getNextStepIndex() {
        Properties jobContextProperties = stepContext.getProperties();
        String stepNextIndexString = jobContextProperties.getProperty(StepContextPropertyNames.STEP_NEXT_INDEX);
        return stepNextIndexString != null ? Integer.parseInt(stepNextIndexString) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getStepProperty(String stepPropertyName, Class<T> type) throws KapuaIllegalArgumentException {
        Properties jobContextProperties = stepContext.getProperties();
        String stepPropertyString = jobContextProperties.getProperty(stepPropertyName);

        T stepProperty;
        if (type == String.class) {
            stepProperty = (T) stepPropertyString;
        } else if (type == Integer.class) {
            stepProperty = (T) Integer.valueOf(stepPropertyString);
        } else if (type == Long.class) {
            stepProperty = (T) Long.valueOf(stepPropertyString);
        } else if (type == Float.class) {
            stepProperty = (T) Float.valueOf(stepPropertyString);
        } else if (type == Double.class) {
            stepProperty = (T) Double.valueOf(stepPropertyString);
        } else if (type == Boolean.class) {
            stepProperty = (T) Boolean.valueOf(stepPropertyString);
        } else if (type == byte[].class || type == Byte[].class) {
            stepProperty = (T) DatatypeConverter.parseBase64Binary(stepPropertyString);
        } else {
            try {
                stepProperty = XmlUtil.unmarshal(stepPropertyString, type);
            } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
                throw new KapuaIllegalArgumentException(stepPropertyName, stepPropertyString);
            }
        }

        return stepProperty;
    }

    @Override
    public String getStepName() {
        return stepContext.getStepName();
    }

    @Override
    public Object getTransientUserData() {
        return stepContext.getTransientUserData();
    }

    @Override
    public void setTransientUserData(Object data) {
        this.stepContext.setTransientUserData(data);
    }

    @Override
    public long getStepExecutionId() {
        return stepContext.getStepExecutionId();
    }

    @Override
    public Properties getProperties() {
        return stepContext.getProperties();
    }

    @Override
    public Serializable getPersistentUserData() {
        return stepContext.getPersistentUserData();
    }

    @Override
    public void setPersistentUserData(Serializable data) {
        this.stepContext.setPersistentUserData(data);
    }

    @Override
    public BatchStatus getBatchStatus() {
        return stepContext.getBatchStatus();
    }

    @Override
    public String getExitStatus() {
        return stepContext.getExitStatus();
    }

    @Override
    public void setExitStatus(String status) {
        this.stepContext.setExitStatus(status);
    }

    @Override
    public Exception getException() {
        return stepContext.getException();
    }

    @Override
    public Metric[] getMetrics() {
        return stepContext.getMetrics();
    }
}
