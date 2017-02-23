/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.management.KapuaMethod;

/**
 * Dictionary class to define actions translations between Kura domain to Kapua domain.<br>
 * For detail about action please refer to {@link KapuaMethod} and {@link KuraMethod}
 * 
 * @since 1.0
 *
 */
public class MethodDictionaryKuraKapua
{
    /**
     * Translations dictionary map
     */
    private final static Map<KuraMethod, KapuaMethod> dictionary;

    static {
        dictionary = new HashMap<>(5);

        dictionary.put(KuraMethod.GET, KapuaMethod.READ);
        dictionary.put(KuraMethod.POST, KapuaMethod.CREATE);
        dictionary.put(KuraMethod.PUT, KapuaMethod.WRITE);
        dictionary.put(KuraMethod.DEL, KapuaMethod.DELETE);
        dictionary.put(KuraMethod.EXEC, KapuaMethod.EXECUTE);
    }

    /**
     * Returns the action translation from Kura domain to Kapua domain
     * 
     * @param kuraMethod
     * @return
     */
    public static KapuaMethod get(KuraMethod kuraMethod)
    {
        return dictionary.get(kuraMethod);
    }
}
