/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.message.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessageFactory;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * {@link KapuaAppProperties} {@link XmlAdapter}.
 *
 * @since 1.0.0
 */
public class KapuaAppPropertiesXmlAdapter extends XmlAdapter<String, KapuaAppProperties> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaRequestMessageFactory REQUEST_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaRequestMessageFactory.class);

    @Override
    public String marshal(KapuaAppProperties kapuaAppProperties) throws Exception {
        return kapuaAppProperties.getValue();
    }

    @Override
    public KapuaAppProperties unmarshal(String string) throws Exception {
        return REQUEST_MESSAGE_FACTORY.newAppProperties(string);
    }
}
