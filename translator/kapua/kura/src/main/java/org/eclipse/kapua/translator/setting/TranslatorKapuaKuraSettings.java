/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link TranslatorKapuaKuraSettings} for {@code kapua-translator-kapua-kura} module.
 *
 * @see AbstractKapuaSetting
 * @since 2.1.0
 */
public class TranslatorKapuaKuraSettings extends AbstractKapuaSetting<TranslatorKapuaKuraSettingKeys> {

    /**
     * Setting filename.
     *
     * @since 2.1.0
     */
    private static final String TRANSLATOR_KAPUA_KURA_SETTING_RESOURCE = "translator-kapua-kura-settings.properties";

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public TranslatorKapuaKuraSettings() {
        super(TRANSLATOR_KAPUA_KURA_SETTING_RESOURCE);
    }
}
