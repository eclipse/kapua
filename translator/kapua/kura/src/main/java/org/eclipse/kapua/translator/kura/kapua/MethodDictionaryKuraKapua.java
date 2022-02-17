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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;

import java.util.EnumMap;
import java.util.Map;

/**
 * Dictionary class to define actions translations between {@link org.eclipse.kapua.service.device.call.kura.Kura} domain to Kapua domain.
 *
 * @see KapuaMethod
 * @see KuraMethod
 * @since 1.0.0
 */
public class MethodDictionaryKuraKapua {

    /**
     * Translations dictionary map
     */
    private static final Map<KuraMethod, KapuaMethod> DICTIONARY;

    static {
        DICTIONARY = new EnumMap<>(KuraMethod.class);

        DICTIONARY.put(KuraMethod.GET, KapuaMethod.READ);
        DICTIONARY.put(KuraMethod.POST, KapuaMethod.CREATE);
        DICTIONARY.put(KuraMethod.PUT, KapuaMethod.WRITE);
        DICTIONARY.put(KuraMethod.DEL, KapuaMethod.DELETE);
        DICTIONARY.put(KuraMethod.EXEC, KapuaMethod.EXECUTE);
        DICTIONARY.put(KuraMethod.SUBMIT, KapuaMethod.SUBMIT);
        DICTIONARY.put(KuraMethod.CANCEL, KapuaMethod.CANCEL);
    }

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private MethodDictionaryKuraKapua() {
    }

    /**
     * Returns the method translation from Kura domain to Kapua domain
     *
     * @param kuraMethod The {@link KuraMethod} to translate.
     * @return The method translation from Kura domain to Kapua domain
     * @since 1.0.0
     * @deprecated Since 1.2.0. Renamed to
     */
    @Deprecated
    public static KapuaMethod get(KuraMethod kuraMethod) {
        return translate(kuraMethod);
    }

    /**
     * Translates the given {@link KuraMethod} to the matching {@link KapuaMethod}.
     *
     * @param kuraMethod The {@link KuraMethod} to translate.
     * @return The matching {@link KapuaMethod}
     * @since 1.2.0
     */
    public static KapuaMethod translate(KuraMethod kuraMethod) {
        return DICTIONARY.get(kuraMethod);
    }
}
