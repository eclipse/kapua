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
 *      Eurotech - initial APInd implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.RandomUtils;

import java.math.BigInteger;
import java.util.Random;

/**
 * Generates random identifier
 *
 * @since 1.0.0
 */
public class IdGenerator {

    private static final Random RANDOM = RandomUtils.getInstance();
    private static final int ID_SIZE = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_KEY_SIZE);

    private IdGenerator() {
    }

    /**
     * Generate a {@link BigInteger} random value.<br>
     * For more detail refer to: {@link SystemSettingKey#KAPUA_KEY_SIZE}
     *
     * @return
     */
    public static BigInteger generate() {
        return new BigInteger(ID_SIZE, RANDOM);
    }

}
