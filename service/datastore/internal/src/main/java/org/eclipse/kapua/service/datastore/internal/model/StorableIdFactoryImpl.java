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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.StorableIdFactory;

/**
 * Storable identifier factory reference implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class StorableIdFactoryImpl implements StorableIdFactory {

    @Override
    public StorableId newStorableId(String stringId) {
        return new StorableIdImpl(stringId);
    }
}
