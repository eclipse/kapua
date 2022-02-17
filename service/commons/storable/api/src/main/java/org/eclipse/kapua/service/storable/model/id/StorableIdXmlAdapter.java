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
package org.eclipse.kapua.service.storable.model.id;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * {@link StorableId} {@link XmlAdapter}.
 * <p>
 * It marshals and unmarshals the {@link StorableId}.
 *
 * @since 1.0.0
 */
public class StorableIdXmlAdapter extends XmlAdapter<String, StorableId> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final StorableIdFactory STORABLE_ID_FACTORY = LOCATOR.getFactory(StorableIdFactory.class);

    @Override
    public String marshal(StorableId storableId) {
        return storableId.toString();
    }

    @Override
    public StorableId unmarshal(String storableIdString) {
        return STORABLE_ID_FACTORY.newStorableId(storableIdString);
    }

}
