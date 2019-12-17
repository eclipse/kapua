/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultBuilderRegistry implements BuilderRegistry {

    private Map<String, ServiceBuilder<?, ?>> builders = new HashMap<>();

    public void register(String aName, ServiceBuilder<?, ?> aBuilder) {
        builders.put(aName, aBuilder);
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(builders.keySet());
    }

    public ServiceBuilder<?, ?> get(String aName) {
        return builders.get(aName);
    }
}
