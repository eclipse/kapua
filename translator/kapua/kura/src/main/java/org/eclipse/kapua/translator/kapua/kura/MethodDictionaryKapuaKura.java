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
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;

import java.util.EnumMap;
import java.util.Map;

/**
 * Dictionary class to define actions translations between Kapua domain to {@link org.eclipse.kapua.service.device.call.kura.Kura} domain.<br>
 *
 * @see KapuaMethod
 * @see KuraMethod
 * @since 1.0.0
 */
public class MethodDictionaryKapuaKura {

    /**
     * Translations dictionary map.
     *
     * @since 1.0.0
     */
    private static final Map<KapuaMethod, KuraMethod> DICTIONARY;

    static {
        DICTIONARY = new EnumMap<>(KapuaMethod.class);

        DICTIONARY.put(KapuaMethod.READ, KuraMethod.GET);
        DICTIONARY.put(KapuaMethod.GET, KuraMethod.GET);
        DICTIONARY.put(KapuaMethod.CREATE, KuraMethod.POST);
        DICTIONARY.put(KapuaMethod.POST, KuraMethod.POST);
        DICTIONARY.put(KapuaMethod.WRITE, KuraMethod.PUT);
        DICTIONARY.put(KapuaMethod.PUT, KuraMethod.PUT);
        DICTIONARY.put(KapuaMethod.DELETE, KuraMethod.DEL);
        DICTIONARY.put(KapuaMethod.DEL, KuraMethod.DEL);
        DICTIONARY.put(KapuaMethod.EXECUTE, KuraMethod.EXEC);
        DICTIONARY.put(KapuaMethod.EXEC, KuraMethod.EXEC);
        DICTIONARY.put(KapuaMethod.SUBMIT, KuraMethod.SUBMIT);
        DICTIONARY.put(KapuaMethod.CANCEL, KuraMethod.CANCEL);
    }

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private MethodDictionaryKapuaKura() {
    }

    /**
     * Gets the given {@link KapuaMethod} in the matching {@link KuraMethod}
     *
     * @param kapuaMethod The {@link KapuaMethod} to match.
     * @return The matching {@link KuraMethod}
     * @since 1.0.0
     * @deprecated Since 1.2.0. Renamed to {@link #translate(KapuaMethod)}
     */
    @Deprecated
    public static KuraMethod get(KapuaMethod kapuaMethod) {
        return translate(kapuaMethod);
    }

    /**
     * Translates the given {@link KapuaMethod} in the matching {@link KuraMethod}
     *
     * @param kapuaMethod The {@link KapuaMethod} to match.
     * @return The matching {@link KuraMethod}
     * @since 1.2.0
     */
    public static KuraMethod translate(KapuaMethod kapuaMethod) {
        return DICTIONARY.get(kapuaMethod);
    }
}
