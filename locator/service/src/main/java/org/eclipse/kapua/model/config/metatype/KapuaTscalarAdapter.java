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
package org.eclipse.kapua.model.config.metatype;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Kapua scalar adapter. It marshal and unmarshal the Kapua scalar in a proper way.
 * 
 * @since 1.0
 *
 */
public class KapuaTscalarAdapter extends XmlAdapter<String, KapuaTscalar>{

    /**
     * Locator instance
     */
	private final KapuaLocator locator = KapuaLocator.getInstance();

    /**
     * Meta type factory instance
     */
	private final KapuaMetatypeFactory metatypeFactory = locator.getFactory(KapuaMetatypeFactory.class);

	@Override
	public KapuaTscalar unmarshal(String v) throws Exception {
		return metatypeFactory.newKapuaTscalar();
	}

	@Override public String marshal(KapuaTscalar v) throws Exception {
		return v.value();
	}

}
