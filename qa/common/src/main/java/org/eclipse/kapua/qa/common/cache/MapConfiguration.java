/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cache;

import javax.cache.configuration.Configuration;

public class MapConfiguration<K, V> implements Configuration<K, V> {

    @Override
    public Class<K> getKeyType() {
        return (Class<K>) Object.class;
    }

    @Override
    public Class<V> getValueType() {
        return (Class<V>) Object.class;
    }

    @Override
    public boolean isStoreByValue() {
        throw new UnsupportedOperationException();
    }
}
