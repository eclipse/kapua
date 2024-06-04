/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleJaxbClassProvider implements JaxbClassProvider {

    private final List<Class<?>> classes;

    public SimpleJaxbClassProvider(Class<?>... classes) {
        this.classes = Arrays.asList(classes);
    }

    @Override
    public Collection<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public String toString() {
        return "SimpleJaxbClassProvider{" +
                "classes=" + classes +
                '}';
    }
}
