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
package org.eclipse.kapua.microservice.jobengine;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreXmlRegistry;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.KapuaTscalarAdapter;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

import javax.xml.bind.JAXBContext;

public class JobEngineJaxbContextProvider implements JAXBContextProvider {

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                context = JAXBContextFactory.createContext(new Class<?>[] {

                        KapuaTocd.class,
                        KapuaTad.class,
                        KapuaTicon.class,
                        KapuaToption.class,
                        KapuaTmetadata.class,
                        KapuaTscalarAdapter.class,
                        TscalarImpl.class,
                        MetatypeXmlRegistry.class,

                        // Job
                        JobTargetSublist.class,

                        // KapuaEvent
                        ServiceEvent.class,
                        EventStoreRecordCreator.class,
                        EventStoreRecordListResult.class,
                        EventStoreRecordQuery.class,
                        EventStoreXmlRegistry.class

                }, null);
            }
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
