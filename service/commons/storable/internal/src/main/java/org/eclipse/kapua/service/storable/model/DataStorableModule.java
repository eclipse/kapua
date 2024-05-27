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
package org.eclipse.kapua.service.storable.model;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.JaxbClassProvider;
import org.eclipse.kapua.commons.core.SimpleJaxbClassProvider;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.SortField;

import com.google.inject.multibindings.Multibinder;

public class DataStorableModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        final Multibinder<JaxbClassProvider> jaxbClassProviderMultibinder = Multibinder.newSetBinder(binder(), JaxbClassProvider.class);
        jaxbClassProviderMultibinder.addBinding()
                .toInstance(new SimpleJaxbClassProvider(
                                SortField.class,
                                StorableId.class
                        )
                );
    }
}
