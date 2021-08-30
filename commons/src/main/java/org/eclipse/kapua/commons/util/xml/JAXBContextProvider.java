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
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import org.eclipse.kapua.KapuaException;

import javax.xml.bind.JAXBContext;

/**
 * {@link JAXBContextProvider} definition.
 *
 * @since 1.0.0
 */
public interface JAXBContextProvider {

    /**
     * Gets the {@link JAXBContext}
     *
     * @return The {@link JAXBContext}
     * @throws KapuaException
     * @since 1.0.0
     * @deprecated since 2.0.0. Make use of {@link #getJAXBContext(Class)} which supports by-type JAXBContext. Invoking this may return the undesired JAXBContext.
     */
    @Deprecated
    JAXBContext getJAXBContext() throws KapuaException;

    /**
     * Gets the {@link JAXBContext}
     *
     * @return The {@link JAXBContext}
     * @throws KapuaException
     * @since 1.5.0
     */
    JAXBContext getJAXBContext(Class<?> clazz) throws KapuaException;
}
