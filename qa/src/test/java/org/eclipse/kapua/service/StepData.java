/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Simple container for between step data that is persisted between steps and
 * between step implementation classes.
 */
@Singleton
public class StepData {

    /**
     * Generic map that accepts string key that represents data and data
     * as any object.
     * Dev-user has to know type of data stored under specified key.
     * Key could be class name.
     */
    Map<String, Object> stepDataMap;

    public StepData() {
        stepDataMap = new HashMap<>();
    }

    public void clear() {
        stepDataMap.clear();
    }

    public void put(String key, Object value) {
        stepDataMap.put(key, value);
    }

    public Object get(String key) {
        return stepDataMap.get(key);
    }

    public void remove(String key) {
        stepDataMap.remove(key);
    }
}
