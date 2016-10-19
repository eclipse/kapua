/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *
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
