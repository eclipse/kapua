/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.processor.datastore.hono;

import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.processor.commons.HttpServiceVerticle;
import org.eclipse.kapua.processor.commons.JAXBContextProviderImpl;
import org.eclipse.kapua.processor.commons.MessageProcessorVerticle;

public class ProcessorContextConfig extends ObjectContextConfig {

    public ProcessorContextConfig() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(MainVerticle.class);
        bind(HttpServiceVerticle.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class);
        bind(MessageProcessorVerticle.class).to(HonoDatastoreProcessorVerticle.class);
        bind(HonoDatastoreProcessorConfigFactory.class);
    }

}
