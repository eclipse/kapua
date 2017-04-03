/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial APInd implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.id;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

/**
 * Generates random identifier
 *
 * @since 1.0
 *
 */
public class IdGenerator {

    private final static SecureRandom secureRandom = new SecureRandom();
    private final static int ID_SIZE = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_KEY_SIZE);

    /**
     * Generate a {@link BigInteger} random value.<br>
     * For more detail refer to: {@link SystemSettingKey#KAPUA_KEY_SIZE}
     *
     * @return
     */
    public static BigInteger generate() {
        byte[] bytes = new byte[ID_SIZE];
        secureRandom.nextBytes(bytes);
        return new BigInteger(bytes);
    }

}
