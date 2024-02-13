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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.security.registration;

import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;

import javax.inject.Singleton;

public class RegistrationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {

    }

    @ProvidesIntoSet
    @Singleton
    RegistrationProcessorProvider noopRegistrationProcessor() {
        return new NoopRegistrationProcessor();
    }
}
