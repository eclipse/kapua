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
package org.eclipse.kapua.model.id;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Kapua identifier adapter. It marshal and unmarshal the Kapua identifier in a proper way.
 * 
 * @since 1.0
 *
 */
public class KapuaIdAdapter extends XmlAdapter<String, KapuaId> {

    /**
     * Locator instance
     */
    private final KapuaLocator locator = KapuaLocator.getInstance();

    /**
     * Meta type factory instance
     */
    private final KapuaIdFactory kapuaIdFactory = locator.getFactory(KapuaIdFactory.class);

    @Override
    public String marshal(KapuaId v) throws Exception {
        return v != null ? v.toCompactId() : null;
    }

    @Override
    public KapuaId unmarshal(String v) throws Exception {
        return v != null ? kapuaIdFactory.newKapuaId(v) : null;
    }

}
