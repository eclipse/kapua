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
package org.eclipse.kapua.message.internal;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.ClassProvider;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;

import com.google.inject.multibindings.Multibinder;

public class MessageModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(KapuaMessageFactory.class).to(KapuaMessageFactoryImpl.class);
        final Multibinder<ClassProvider> classProviderBinder = Multibinder.newSetBinder(binder(), ClassProvider.class);
        classProviderBinder.addBinding()
                .toInstance(new ClassProvider() {

                    @Override
                    public Collection<Class<?>> getClasses() {
                        return Arrays.asList(
                                KapuaDataChannel.class,
                                KapuaDataPayload.class
                        );
                    }
                });
    }
}
