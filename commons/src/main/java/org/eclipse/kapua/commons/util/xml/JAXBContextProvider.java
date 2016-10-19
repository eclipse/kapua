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
