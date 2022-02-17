/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

/**
 * Setting key reference implementation.
 *
 * @since 1.0
 *
 */
public class SimpleSettingKey implements SettingKey {

    private final String key;

    /**
     * Constructor
     *
     * @param key
     */
    public SimpleSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

}
