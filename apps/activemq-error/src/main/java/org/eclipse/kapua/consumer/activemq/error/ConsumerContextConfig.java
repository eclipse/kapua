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
package org.eclipse.kapua.consumer.activemq.error;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.apps.api.MessageConsumerServerConfig;
import org.eclipse.kapua.apps.api.HttpRestServerImpl;
import org.eclipse.kapua.apps.api.JAXBContextProviderImpl;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

public class ConsumerContextConfig extends ObjectContextConfig {

    public ConsumerContextConfig() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(MainVerticle.class);
        bind(ConnectionConfiguration.class).in(Singleton.class);
        bind(SourceConfiguration.class).in(Singleton.class);
        bind(HttpRestServer.class).to(HttpRestServerImpl.class).in(Singleton.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class).in(Singleton.class);
        bind(new TypeLiteral<ObjectFactory<MessageConsumerServerConfig<Message, Message>>>() {}).to(AmqpErrorConsumerServerConfigFactory.class);
    }

}
