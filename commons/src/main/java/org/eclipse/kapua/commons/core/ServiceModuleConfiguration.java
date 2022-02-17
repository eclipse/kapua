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
package org.eclipse.kapua.commons.core;

import java.util.Set;

public class ServiceModuleConfiguration {

    public interface ConfigurationProvider {
        ServiceModuleProvider get() ;
    }

    private static ConfigurationProvider cofigurationProvider;

    private ServiceModuleConfiguration() {}

    public static void setConfigurationProvider(ConfigurationProvider aProvider) {
        cofigurationProvider = aProvider;
    }

    public static Set<ServiceModule> getServiceModules() {
        return cofigurationProvider.get().getModules();
    }

 }
