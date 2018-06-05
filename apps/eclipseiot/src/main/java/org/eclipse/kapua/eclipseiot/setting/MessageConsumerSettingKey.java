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
package org.eclipse.kapua.eclipseiot.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Broker settings
 */
public enum MessageConsumerSettingKey implements SettingKey {

    /**
     * Verticle class implementation to instantiate
     */
    VERTICLE_CLASS_NAME("eclipseiot.verticle.class"),
    /**
     * Converter class
     */
    CONVERTER_CLASS_NAME("eclipseiot.converter.class"),
    /**
     * Processor class
     */
    PROCESSOR_CLASS_NAME("eclipseiot.processor.class");

    private String key;

    private MessageConsumerSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
