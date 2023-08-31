/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class ConsoleSetting extends AbstractKapuaSetting<ConsoleSettingKeys> {

    private static final String CONSOLE_SETTING_RESOURCE = "console-setting.properties";

    private static final ConsoleSetting INSTANCE = new ConsoleSetting();

    private ConsoleSetting() {
        super(CONSOLE_SETTING_RESOURCE);
    }

    public static ConsoleSetting getInstance() {
        return INSTANCE;
    }
}
