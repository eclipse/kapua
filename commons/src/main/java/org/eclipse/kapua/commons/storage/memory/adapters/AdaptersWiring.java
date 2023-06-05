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
package org.eclipse.kapua.commons.storage.memory.adapters;

import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.storage.memory.KapuaQueryConverter;

public class AdaptersWiring extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(KapuaQueryConverter.class).to(KapuaQueryConverterImpl.class);
    }

    @ProvidesIntoSet
    QueryPredicateResolver andPredicate() {
        return new AndPredicateResolver();
    }

    @ProvidesIntoSet
    QueryPredicateResolver orPredicateResolver() {
        return new OrPredicateResolver();
    }

    @ProvidesIntoSet
    QueryPredicateResolver attributePredicateResolver() {
        return new AttributePredicateResolver();
    }
}
