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
package org.eclipse.kapua.commons.service.internal;

public class CacheType {

    private String key;

    public CacheType(Class keyClass, Class entityClass) {
        key = keyClass.getSimpleName() + "|" + entityClass.getSimpleName();
    }

    public String getKey() {
        return key;
    }
}
