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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Kapua identifier adapter. It marshal and unmarshal the Kapua identifier in a proper way.
 *
 * @since 1.0
 */
public class StorableIdAdapter extends XmlAdapter<String, StorableId> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final StorableIdFactory STORABLE_ID_FACTORY = LOCATOR.getFactory(StorableIdFactory.class);

    @Override
    public String marshal(StorableId v) throws Exception {
        return v.toString();
    }

    @Override
    public StorableId unmarshal(String v) throws Exception {
        return STORABLE_ID_FACTORY.newStorableId(v);
    }

}
