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
package org.eclipse.kapua.sso.provider.generic.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class GenericSsoSetting extends AbstractKapuaSetting<GenericSsoSettingKeys> {

    private static final String GENERIC_SSO_SETTING_RESOURCE = "sso-generic-setting.properties";

    private static final GenericSsoSetting INSTANCE = new GenericSsoSetting();

    private GenericSsoSetting() {
        super(GENERIC_SSO_SETTING_RESOURCE);
    }
    
    GenericSsoSetting(final String resourceName) {
        super(resourceName);
    }

    public static GenericSsoSetting getInstance() {
        return INSTANCE;
    }
}
