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
package org.eclipse.kapua.translator.kapua.kura;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.management.KapuaMethod;

/**
 * Dictionary class to define actions translations between Kapua domain to Kura domain.<br>
 * For detail about action please refer to {@link KapuaMethod} and {@link KuraMethod}
 * 
 * @since 1.0
 *
 */
public class MethodDictionaryKapuaKura
{
    /**
     * Translations dictionary map
     */
    private final static Map<KapuaMethod, KuraMethod> dictionary;

    static {
        dictionary = new HashMap<>(5);

        dictionary.put(KapuaMethod.READ, KuraMethod.GET);
        dictionary.put(KapuaMethod.CREATE, KuraMethod.POST);
        dictionary.put(KapuaMethod.WRITE, KuraMethod.PUT);
        dictionary.put(KapuaMethod.DELETE, KuraMethod.DEL);
        dictionary.put(KapuaMethod.EXECUTE, KuraMethod.EXEC);
    }

    /**
     * Returns the action translation from Kapua domain to Kura domain
     * 
     * @param kapuaMethod
     * @return
     */
    public static KuraMethod get(KapuaMethod kapuaMethod)
    {
        return dictionary.get(kapuaMethod);
    }
}
