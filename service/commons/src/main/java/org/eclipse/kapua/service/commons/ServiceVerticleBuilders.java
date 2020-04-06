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

public class ServiceVerticleBuilders {

    private Map<String, ServiceVerticleBuilder<?, ? extends ServiceVerticle>> builders = new HashMap<>();

    public Set<String> getNames() {
        return Collections.unmodifiableSet(builders.keySet());
    }

    public ServiceVerticleBuilders put(String name, ServiceVerticleBuilder<?, ? extends ServiceVerticle> builder) {
        builders.put(name, builder);
        return this;
    }

    public ServiceVerticleBuilder<?, ? extends ServiceVerticle> get(String name) {
        return builders.get(name);
    }
}