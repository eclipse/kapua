package org.eclipse.kapua.commons.util.xml;

import javax.xml.bind.JAXBContext;

import org.eclipse.kapua.KapuaException;

public interface JAXBContextProvider
{
	public JAXBContext getJAXBContext() throws KapuaException ;
	//public JAXBContext newJAXBContext() throws KapuaException ;
}
