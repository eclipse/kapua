/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.setting.system;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * System setting implementation.<br>
 * This class handles settings for the {@link SystemSettingKey}.
 *
 * @since 1.0
 */
public class SystemSetting extends AbstractKapuaSetting<SystemSettingKey> {

    /**
     * Constant representing name of the resource properties file used by this settings.
     */
    private static final String CONFIG_RESOURCE_NAME = "kapua-environment-setting.properties";

    private static final SystemSetting INSTANCE = new SystemSetting();
    private final static String COMMONS_CONTROL_MESSAGE_CLASSIFIER = "commons.control_message.classifier";

    // Constructors

    private SystemSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    public String getMessageClassifier() throws KapuaRuntimeException {
        String classifier = config.getString(COMMONS_CONTROL_MESSAGE_CLASSIFIER);
        if (classifier.matches("([#>\\./\\+\\*‌​])")) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, "The message classifier cannot contains special chars ('.', '/', '+', '*', '/', '>'");
        }
        return classifier;
    }

    // Accessors

    /**
     * Return the singleton system setting instance.
     *
     * @return singleton system setting instance
     */
    public static SystemSetting getInstance() {
        return INSTANCE;
    }

}