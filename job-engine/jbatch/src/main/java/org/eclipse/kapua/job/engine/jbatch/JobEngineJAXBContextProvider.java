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
package org.eclipse.kapua.job.engine.jbatch;

import javax.xml.bind.JAXBContext;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

public class JobEngineJAXBContextProvider implements JAXBContextProvider {

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                context = JAXBContextFactory.createContext(new Class<?>[] {

                        DeviceCommandInput.class,
                        DeviceCommandOutput.class,

                }, null);
            }
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
