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
 *******************************************************************************/
package org.eclipse.kapua.model.id;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Kapua identifier adapter. It marshal and unmarshal the Kapua identifier in a proper way.
 *
 * @since 1.0
 */
public class KapuaIdAdapter extends XmlAdapter<String, KapuaId> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaIdFactory KAPUA_ID_FACTORY = LOCATOR.getFactory(KapuaIdFactory.class);

    @Override
    public String marshal(KapuaId v) throws Exception {
        return v != null ? v.toCompactId() : null;
    }

    @Override
    public KapuaId unmarshal(String v) throws Exception {
        return v != null ? KAPUA_ID_FACTORY.newKapuaId(v) : null;
    }
}
