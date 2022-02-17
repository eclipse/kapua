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

import javax.xml.bind.JAXBContext;

import org.eclipse.kapua.KapuaException;

/**
 * Jaxb context provider service definition.
 *
 * @since 1.0
 *
 */
public interface JAXBContextProvider {

    /**
     * Get the jaxb context
     *
     * @return
     * @throws KapuaException
     */
    public JAXBContext getJAXBContext() throws KapuaException;
}
