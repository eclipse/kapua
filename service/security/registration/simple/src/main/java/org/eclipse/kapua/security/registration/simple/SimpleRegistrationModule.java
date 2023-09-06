/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.security.registration.simple;

import com.google.inject.multibindings.Multibinder;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.security.registration.RegistrationProcessorProvider;
import org.eclipse.kapua.security.registration.simple.setting.SimpleSetting;

import javax.inject.Singleton;

public class SimpleRegistrationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(SimpleSetting.class).in(Singleton.class);
        Multibinder<RegistrationProcessorProvider> registrationProcessorProviderMultibinder = Multibinder.newSetBinder(binder(), RegistrationProcessorProvider.class);
        registrationProcessorProviderMultibinder.addBinding().to(SimpleRegistrationProcessorProvider.class).in(Singleton.class);
    }
}
