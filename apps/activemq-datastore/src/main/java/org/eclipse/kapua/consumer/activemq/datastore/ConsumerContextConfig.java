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
package org.eclipse.kapua.consumer.activemq.datastore;

import org.eclipse.kapua.apps.api.MessageConsumerServerConfig;
import org.eclipse.kapua.apps.api.HttpRestServerImpl;
import org.eclipse.kapua.apps.api.JAXBContextProviderImpl;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.message.transport.TransportMessage;

import com.google.inject.TypeLiteral;

public class ConsumerContextConfig extends ObjectContextConfig {

    public ConsumerContextConfig() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(MainVerticle.class);
        bind(ConnectionConfiguration.class);
        bind(SourceConfiguration.class);
        bind(TargetConfiguration.class);
        bind(HttpRestServer.class).to(HttpRestServerImpl.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class);
        bind(new TypeLiteral<ObjectFactory<MessageConsumerServerConfig<byte[],TransportMessage>>>() {}).to(AmqpDatastoreConsumerServerConfigFactory.class);
    }

}
