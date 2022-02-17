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

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link StorableIdFactory} definition.
 * <p>
 * It is the {@link KapuaObjectFactory} for {@link StorableId}s.
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
