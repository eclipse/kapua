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

public class KapuaTscalarAdapter extends XmlAdapter<String, KapuaTscalar> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaMetatypeFactory METATYPE_FACTORY = LOCATOR.getFactory(KapuaMetatypeFactory.class);

    @Override
    public String marshal(KapuaTscalar v) throws Exception {
        return v.value();
    }

    @Override
    public KapuaTscalar unmarshal(String v) throws Exception {
        return METATYPE_FACTORY.newKapuaTscalar(v);
    }
}
