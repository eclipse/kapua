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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.model.config.metatype.*;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserXmlRegistry;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class RegistryJAXBContextProvider implements JAXBContextProvider {

    private static final Logger logger = LoggerFactory.getLogger(RegistryJAXBContextProvider.class);

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        if (context == null) {
            Class<?>[] classes = new Class<?>[] {
                    User.class,
                    UserListResult.class,
                    UserXmlRegistry.class,
                    KapuaTmetadata.class,
                    KapuaTocd.class,
                    KapuaTad.class,
                    KapuaTicon.class,
                    TscalarImpl.class,
                    KapuaToption.class,
                    KapuaTdesignate.class,
                    KapuaTobject.class,
                    MetatypeXmlRegistry.class
            };
            try {
                context = JAXBContextFactory.createContext(classes, null);
            } catch (JAXBException jaxbException) {
                logger.warn("Error creating JAXBContext, tests will fail!");
            }
        }
        return context;
    }
}
