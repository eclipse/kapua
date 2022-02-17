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
package org.eclipse.kapua.commons.core;

import java.util.List;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * A class that provides JAXB serializable classes as exported by {@link KapuaLocator}
 * implementation. KapuaLocator is initialized in the constructor in order let the
 * implementation export the required list of classes.
 *
 * @since 2.0.0
 */
public class ServiceModuleJaxbClassProvider implements ClassProvider {

    public ServiceModuleJaxbClassProvider() {
        // Initialize the Kapua locator to force populate ServiceModuleJaxbConfiguration
        KapuaLocator.getInstance();
    }

    @Override
    public List<Class<?>> getClasses() {
        return ServiceModuleJaxbClassConfig.getXmlSerializables();
    }
}
