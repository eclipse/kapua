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

import org.eclipse.kapua.locator.KapuaProvider;

/**
 * {@link StorableIdFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class StorableIdFactoryImpl implements StorableIdFactory {

    @Override
    public StorableId newStorableId(String stringId) {
        return new StorableIdImpl(stringId);
    }
}
