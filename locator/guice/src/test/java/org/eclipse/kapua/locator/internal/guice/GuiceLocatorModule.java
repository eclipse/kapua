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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.internal.guice;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;

public class GuiceLocatorModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(ServiceA.class).to(ServiceAImpl.class);
        bind(FactoryA.class).to(FactoryAImpl.class);
        bind(FactoryB.class).to(FactoryBImpl.class);
        bind(ServiceB.class).to(ServiceBImpl.class);
    }

}
