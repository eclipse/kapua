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
package org.eclipse.kapua.apps.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.MessageSource;
import org.eclipse.kapua.connector.MessageTarget;

public class MessageConsumerServerConfig<M,P> implements MessageConsumer.Builder<M, P> {

    private MessageSource<M> consumer;
    private Converter<M,P> converter;
    private MessageTarget<P> processor;
    private MessageTarget errorProcessor;
    private String ebAddress;
    private String healthCheckEBAddress;
    private JAXBContextProvider jaxbContextProvider;

    private List<HealthCheckProvider> healthCheckProviders = new ArrayList<>();

    @Override
    public MessageSource<M> getConsumer() {
        return consumer;
    }

    public void setConsumer(MessageSource<M> aConsumer) {
        consumer = aConsumer;
    }

    @Override
    public Converter<M, P> getConverter() {
        return converter;
    }

    public void setConverter(Converter<M, P> aConverter) {
        this.converter = aConverter;
    }

    @Override
    public MessageTarget<P> getProcessor() {
        return processor;
    }

    public void setProcessor(MessageTarget<P> aProcessor) {
        processor = aProcessor;
    }

    @Override
    public MessageTarget getErrorProcessor() {
        return errorProcessor;
    }

    public void setErrorProcessor(MessageTarget aProcessor) {
        errorProcessor = processor;
    }

    @Override
    public String getEBAddress() {
        return ebAddress;
    }

    public void setEBAddress(String anEBAddress) {
        ebAddress = anEBAddress;
    }

    @Override
    public String getHealthCheckEBAddress() {
        return healthCheckEBAddress;
    }

    public void setHealthCheckEBAddress(String anEBAddress) {
        healthCheckEBAddress = anEBAddress;
    }

    @Override
    public JAXBContextProvider getJAXBContextProvider() {
        return jaxbContextProvider;
    }

    public void setJAXBContextProvider(JAXBContextProvider aProvider) {
        jaxbContextProvider = aProvider;
    }

    public List<HealthCheckProvider> getHealthCheckProviders() {
        return healthCheckProviders;
    }
}
