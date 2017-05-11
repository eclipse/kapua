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
package org.eclipse.kapua.sso.provider.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class SsoSetting extends AbstractKapuaSetting<SsoSettingKeys> {

    private static final String SSO_SETTING_RESOURCE = "sso-setting.properties";

    private static final SsoSetting INSTANCE = new SsoSetting();

    private SsoSetting() {
        super(SSO_SETTING_RESOURCE);
    }
    
    SsoSetting(final String resourceName) {
        super(resourceName);
    }

    public static SsoSetting getInstance() {
        return INSTANCE;
    }
}
