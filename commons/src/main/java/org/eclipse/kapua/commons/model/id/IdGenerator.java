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
public class IdGenerator
{
    private final static SecureRandom secureRandom = new SecureRandom();
    private static int                ID_SIZE      = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_KEY_SIZE);

    /**
     * Generate a {@link BigInteger} random value.<br>
     * For more detail refer to: {@link SystemSettingKey#KAPUA_KEY_SIZE}
     * 
     * @return
     */
    public static BigInteger generate()
    {
        byte[] bytes = new byte[ID_SIZE];
        secureRandom.nextBytes(bytes);
        return new BigInteger(bytes);
    }

}
