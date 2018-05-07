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

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link StorableId} factory definition.
 *
 * @since 1.0.0
 */
public interface StorableIdFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link StorableId} starting the provided string identifier.<br>
     * <b>This operation must be the inverse function of {@link StorableId#toString()} so, in other word, this code should't fail:
     * </b>
     *
     * <pre>
     * {@code
     * StorableIdFactory storableIdFactory = KapuaLocator.getInstance().getService(StorableIdFactory.class);<br>
     * String shortId = "some well formed encoded short id";
     * StorableId id = storableIdFactory.newStorableId(shortId);
     * String shortIdConverted = id.getShortId();
     * AssertTrue(shortId.equals(shortIdConverted));
     * }
     * </pre>
     *
     * @param stringId The {@link StorableId} short id to parse.
     * @return The {@link StorableId} parsed from its short representation.
     * @since 1.0.0
     */
    StorableId newStorableId(String stringId);

}
