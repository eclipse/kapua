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
package org.eclipse.kapua.job.engine.commons.wrappers;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.id.KapuaId;
import org.xml.sax.SAXException;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import javax.batch.runtime.context.StepContext;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.Serializable;
import java.util.Properties;

public class StepContextWrapper {

    private StepContext stepContext;

    public StepContextWrapper(StepContext stepContext) {
        this.stepContext = stepContext;
    }

    public int getStepIndex() {
        Properties stepContextProperties = stepContext.getProperties();
        String stepIndexString = stepContextProperties.getProperty(StepContextPropertyNames.STEP_INDEX);

        if (Strings.isNullOrEmpty(stepIndexString)) {
            throw KapuaRuntimeException.internalError("stepIndexString is not available in the StepContext.properties");
        }

        return Integer.parseInt(stepIndexString);
    }

    public Integer getNextStepIndex() {
        Properties jobContextProperties = stepContext.getProperties();
        String stepNextIndexString = jobContextProperties.getProperty(StepContextPropertyNames.STEP_NEXT_INDEX);
        return stepNextIndexString != null ? Integer.parseInt(stepNextIndexString) : null;
    }

    public <T> T getStepProperty(String stepPropertyName, Class<T> type) throws KapuaIllegalArgumentException {
        Properties jobContextProperties = stepContext.getProperties();
        String stepPropertyString = jobContextProperties.getProperty(stepPropertyName);

        T stepProperty;
        if (stepPropertyString != null) {
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
            } else if (type == KapuaId.class) {
                stepProperty = (T) KapuaEid.parseCompactId(stepPropertyString);
            } else if (type.isEnum()) {
                Class<? extends Enum> enumType = (Class<? extends Enum>) type;

                try {
                    stepProperty = (T) Enum.valueOf(enumType, stepPropertyString);
                } catch (IllegalArgumentException iae) {
                    throw new KapuaIllegalArgumentException(stepPropertyName, stepPropertyString);
                }
            } else {
                try {
                    stepProperty = XmlUtil.unmarshal(stepPropertyString, type);
                } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
                    throw new KapuaIllegalArgumentException(stepPropertyName, stepPropertyString);
                }
            }
        } else {
            stepProperty = null;
        }

        return stepProperty;
    }

    public String getStepName() {
        return stepContext.getStepName();
    }

    public Object getTransientUserData() {
        return stepContext.getTransientUserData();
    }

    public void setTransientUserData(Object data) {
        stepContext.setTransientUserData(data);
    }

    public long getStepExecutionId() {
        return stepContext.getStepExecutionId();
    }

    public Properties getProperties() {
        return stepContext.getProperties();
    }

    public Serializable getPersistentUserData() {
        return stepContext.getPersistentUserData();
    }

    public void setPersistentUserData(Serializable data) {
        stepContext.setPersistentUserData(data);
    }

    public BatchStatus getBatchStatus() {
        return stepContext.getBatchStatus();
    }

    public String getExitStatus() {
        return stepContext.getExitStatus();
    }

    public void setExitStatus(String status) {
        stepContext.setExitStatus(status);
    }

    public Exception getException() {
        return stepContext.getException();
    }

    public Metric[] getMetrics() {
        return stepContext.getMetrics();
    }
}
