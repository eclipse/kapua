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
import org.eclipse.kapua.connector.Consumer;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.Processor;

public class AmqpConnectorServerConfig<M,P> implements AmqpConnectorServer.Builder<M, P> {

    private Consumer<M> consumer;
    private Converter<M,P> converter;
    private Processor<P> processor;
    private Processor errorProcessor;
    private String ebAddress;
    private String healthCheckEBAddress;
    private JAXBContextProvider jaxbContextProvider;

    private List<HealthCheckProvider> healthCheckProviders = new ArrayList<>();

    @Override
    public Consumer<M> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<M> aConsumer) {
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
    public Processor<P> getProcessor() {
        return processor;
    }

    public void setProcessor(Processor<P> aProcessor) {
        processor = aProcessor;
    }

    @Override
    public Processor getErrorProcessor() {
        return errorProcessor;
    }

    public void setErrorProcessor(Processor aProcessor) {
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
