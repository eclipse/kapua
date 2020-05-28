/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.consumer.commons.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Settings
 */
public enum ConsumerSettingKey implements SettingKey {
    /**
     * Jaxb context provider class name
     */
    JAXB_CONTEXT_CLASS_NAME("consumer.jaxb_context_class_name");

    private String key;

    private ConsumerSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
