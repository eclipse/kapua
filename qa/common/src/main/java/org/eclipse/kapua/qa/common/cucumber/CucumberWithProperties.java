/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Extension of Cucumber junit runner that adds functionality of System properties
 * that are set with CucumberProperty repeatable annotation with key and value attribute
 * that represents system property.
 */
public class CucumberWithProperties extends Cucumber {

    private static final Logger logger = LoggerFactory.getLogger(CucumberWithProperties.class);

    public CucumberWithProperties(Class<?> clazz) throws InitializationError, IOException {
        super(clazz);

        CucumberProperty[] systemProperties = getProperties(clazz);
        for (CucumberProperty property : systemProperties) {
            if ((property.value() == null) || (property.value().length() == 0)) {
                logger.info("Clearing property " + property.key());
                System.clearProperty(property.key());
            } else {
                logger.info("Setting property " + property.key() + ", value is " + property.value());
                System.setProperty(property.key(), property.value());
            }
        }
    }

    /**
     * Extract repeatable CucumberProperty from annotation on junit runner CucumberWithProperties
     *
     * @param clazz Class that is annotated
     * @return property repeatable array of key / value pairs as CucumberProperty class
     */
    private CucumberProperty[] getProperties(Class<?> clazz) {

        return clazz.getAnnotationsByType(CucumberProperty.class);
    }
}
