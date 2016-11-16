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
 *
 *******************************************************************************/
package org.eclipse.kapua.model.query;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;

/**
 * Kapua result list adapter. It marshal and unmarshal the Kapua result list in a proper way.
 * 
 * @since 1.0
 *
 */
public class KapuaListResultAdapter extends XmlAdapter<String, KapuaId>{

    /**
     * Locator instance
     */
	private final KapuaLocator locator = KapuaLocator.getInstance();

    /**
     * Meta type factory instance
     */
	private final KapuaIdFactory kapuaIdFactory = locator.getFactory(KapuaIdFactory.class);

	@Override
	public String marshal(KapuaId v) throws Exception 
	{
        return v.toCompactId();
	}

	@Override
	public KapuaId unmarshal(String v) throws Exception {
		return kapuaIdFactory.newKapuaId(v);
	}

}
