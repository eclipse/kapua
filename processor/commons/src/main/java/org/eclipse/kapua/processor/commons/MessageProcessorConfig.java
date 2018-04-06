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
package org.eclipse.kapua.processor.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.vertx.EventBusServiceConfig;
import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.MessageSource;
import org.eclipse.kapua.connector.MessageTarget;

public class MessageProcessorConfig<M,P> {

    private MessageSource<M> source;
    private Converter<M,P> converter;
    private MessageTarget<P> target;
    private MessageTarget errorTarget;
    private String ebAddress;
    private String healthCheckEBAddress;

    private List<HealthCheckAdapter> healthCheckAdapters = new ArrayList<>();

    private MessageProcessorConfig(String aPrefix, Configuration aConfig) {
        Objects.requireNonNull(aPrefix, "Invalid null prefix");
        Objects.requireNonNull(aConfig, "Invalid null configuration");
        ebAddress = aConfig.getString(aPrefix + ".eventBusServer.defaultAddress");
        healthCheckEBAddress = aConfig.getString(aPrefix + ".eventBusServer.healthAddress");
    }

    public static <M,P> MessageProcessorConfig<M,P> create(String aPrefix, Configuration aConfig) {
        return new MessageProcessorConfig<M,P>(aPrefix, aConfig);
    }

    public MessageSource<M> getMessageSource() {
        return source;
    }

    public void setMessageSource(MessageSource<M> aConsumer) {
        source = aConsumer;
    }

    public Converter<M, P> getConverter() {
        return converter;
    }

    public void setConverter(Converter<M, P> aConverter) {
        this.converter = aConverter;
    }

    public MessageTarget<P> getMessageTarget() {
        return target;
    }

    public void setMessageTarget(MessageTarget<P> aTarget) {
        target = aTarget;
    }

    public MessageTarget getErrorTarget() {
        return errorTarget;
    }

    public void setErrorTarget(MessageTarget aTarget) {
        errorTarget = aTarget;
    }

    public String getEBAddress() {
        return ebAddress;
    }

    public void setEBAddress(String anEBAddress) {
        ebAddress = anEBAddress;
    }

    public String getHealthCheckEBAddress() {
        return healthCheckEBAddress;
    }

    public void setHealthCheckEBAddress(String anEBAddress) {
        healthCheckEBAddress = anEBAddress;
    }

    public List<HealthCheckAdapter> getHealthCheckAdapters() {
        return healthCheckAdapters;
    }

    public EventBusServiceConfig getEventBusServiceConfig() {
        EventBusServiceConfig c = new EventBusServiceConfig();
        c.setAddress(this.getEBAddress());
        c.setHealthCheckAddress(this.getHealthCheckEBAddress());
        return c;
    }
}
