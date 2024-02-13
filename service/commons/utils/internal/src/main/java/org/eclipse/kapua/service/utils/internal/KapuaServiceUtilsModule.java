/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.utils.internal;

import javax.inject.Singleton;

import org.eclipse.kapua.commons.configuration.AccountRelativeFinder;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.utils.KapuaEntityQueryUtil;

import com.google.inject.Provides;

public class KapuaServiceUtilsModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        // nothing to do here
    }

    @Provides
    @Singleton
    KapuaEntityQueryUtil kapuaEntityQueryUtil(AccountRelativeFinder accountRelativeFinder) {
        return new KapuaEntityQueryUtilImpl(accountRelativeFinder);
    }

}
