/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.camel.xml;

import org.eclipse.kapua.KapuaException;

import javax.inject.Provider;

public class ServiceJAXBContextLoaderProvider implements Provider<ServiceJAXBContextLoader> {
    @Override
    public ServiceJAXBContextLoader get() {
        try {
            ServiceJAXBContextLoader serviceJAXBContextLoader = new ServiceJAXBContextLoader();
            serviceJAXBContextLoader.init();
            return serviceJAXBContextLoader;
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
