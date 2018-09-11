/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessageFactory;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class KapuaAppPropertiesXmlAdapter extends XmlAdapter<String, KapuaAppProperties> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaRequestMessageFactory REQUEST_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaRequestMessageFactory.class);

    @Override
    public String marshal(KapuaAppProperties v) throws Exception {
        return v.getValue();
    }

    @Override
    public KapuaAppProperties unmarshal(String v) throws Exception {
        return REQUEST_MESSAGE_FACTORY.newAppProperties(v);
    }
}
