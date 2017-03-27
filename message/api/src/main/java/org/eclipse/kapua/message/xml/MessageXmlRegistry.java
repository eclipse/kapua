/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.xml;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPayload;

/**
 * Message xml factory class
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class MessageXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final KapuaMessageFactory factory = locator.getFactory(KapuaMessageFactory.class);

    public KapuaPayload newPayload() {
        return factory.newPayload();
    }
}
