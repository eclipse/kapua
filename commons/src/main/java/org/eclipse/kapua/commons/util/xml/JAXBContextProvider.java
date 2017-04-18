/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public interface JAXBContextProvider
{

    /**
     * Get the jaxb context
     * 
     * @return
     * @throws KapuaException
     */
	public JAXBContext getJAXBContext() throws KapuaException ;
}
