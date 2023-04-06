/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
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
package org.eclipse.kapua.service.camel.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Settings
 */
public enum ServiceSettingKey implements SettingKey {
    /**
     * Jaxb context provider class name
     */
    JAXB_CONTEXT_CLASS_NAME("service.jaxb_context_class_name");

    private String key;

    private ServiceSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
