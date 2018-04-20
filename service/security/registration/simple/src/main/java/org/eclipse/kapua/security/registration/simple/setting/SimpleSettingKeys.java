/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.security.registration.simple.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key.
 */
public enum SimpleSettingKeys implements SettingKey {
    SIMPLE_ROOT_ACCOUNT("auto.registration.simple.rootAccount"), //
    SIMPLE_MAX_NUMBER_OF_CHILD_USERS("auto.registration.simple.maximumNumberOfChildUsers");

    private String key;

    private SimpleSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
