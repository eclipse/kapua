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
package org.eclipse.kapua.test.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.runners.model.InitializationError;

import java.io.IOException;
import java.util.AbstractMap;

/**
 * Extension of Cucumber junit runner that adds functionality of System properties
 * that are set with CucumberProperties annotation with systemProperties attribute.
 */
public class CucumberWithProperties extends Cucumber {

    public CucumberWithProperties(Class clazz) throws InitializationError, IOException {
        super(clazz);

        CucumberProperties cucumberProperties = getProperties(clazz);
        String[] systemProperties = cucumberProperties.systemProperties();
        for (String property : systemProperties) {
            AbstractMap.SimpleEntry<String, String> keyValue = extractKeyValue(property);
            System.setProperty(keyValue.getKey(), keyValue.getValue());
        }
    }

    /**
     * Extract key / value pair from string that is split in two by equal sign.
     * e.g. user.name=unknown
     *
     * @param property key / value pair separeted by equal sign
     * @return key / value tuple
     */
    private AbstractMap.SimpleEntry extractKeyValue(String property) {
        String[] tokens = property.split("=");
        AbstractMap.SimpleEntry<String, String> keyValue =
                new AbstractMap.SimpleEntry<String, String>(tokens[0],tokens[1]);

        return keyValue;
    }

    /**
     * Extract CucumberProperties from annotation on junit runner CucumberWithProperties
     *
     * @param clazz Class that is annotated
     * @return properties array of key / value pairs
     */
    private CucumberProperties getProperties(Class<?> clazz) {

        return clazz.getAnnotation(CucumberProperties.class);
    }
}
