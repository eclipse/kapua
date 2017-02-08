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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.kapua.locator.KapuaLocator;

public class KapuaTscalarAdapter extends XmlAdapter<String, KapuaTscalar>{

	private final KapuaLocator locator = KapuaLocator.getInstance();
	private final KapuaMetatypeFactory metatypeFactory = locator.getFactory(KapuaMetatypeFactory.class);

	@Override
	public KapuaTscalar unmarshal(String v) throws Exception {
        return metatypeFactory.newKapuaTscalar(v);
	}

	@Override public String marshal(KapuaTscalar v) throws Exception {
		return v.value();
	}

}
