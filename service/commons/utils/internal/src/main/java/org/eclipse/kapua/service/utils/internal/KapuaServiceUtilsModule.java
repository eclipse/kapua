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

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.utils.KapuaEntityQueryUtil;

import javax.inject.Singleton;

public class KapuaServiceUtilsModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(KapuaEntityQueryUtil.class).to(KapuaEntityQueryUtilImpl.class).in(Singleton.class);
        // nothing to do here
    }
}
