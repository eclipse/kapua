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
package org.eclipse.kapua.consumer.commons.xml;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.locator.KapuaLocator;

public class ConsumerJAXBContextInitializer {
    private void init() {
        try {
            //initializing at startup to trigger class discovery and have configuration errors pop up at startup time
            KapuaLocator.getInstance().getFactory(JAXBContextProvider.class).getJAXBContext();
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
