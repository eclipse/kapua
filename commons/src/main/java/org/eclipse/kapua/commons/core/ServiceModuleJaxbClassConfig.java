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

/**
 * Utility class used by the {@link org.eclipse.kapua.locator.KapuaLocator} implementation to export configuration 
 * regarding JAXB serializable classes defined within Kapua modules.
 *
 * @since 2.0.0
 */
public class ServiceModuleJaxbClassConfig {

    private static List<Class<?>> serializables;

    private ServiceModuleJaxbClassConfig() {}

    public static List<Class<?>> getXmlSerializables() {
        return ServiceModuleJaxbClassConfig.serializables;
    }

    public static void setSerializables(List<Class<?>> xmlSerializables) {
        ServiceModuleJaxbClassConfig.serializables = xmlSerializables;
    }
 }