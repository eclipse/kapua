/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura;

import com.google.common.collect.Lists;
import org.eclipse.kapua.service.device.call.message.DeviceChannel;

import java.util.Arrays;
import java.util.List;

/**
 * {@link DeviceChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraChannel implements DeviceChannel {

    protected String messageClassification;
    protected String scopeNamespace;
    protected String clientId;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraChannel() {
    }

    /**
     * Constructor.
     *
     * @param scopeNamespace The scope namespace.
     * @param clientId       The clientId.
     * @since 1.0.0
     */
    public KuraChannel(String scopeNamespace, String clientId) {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId.
     * @since 1.0.0
     */
    public KuraChannel(String messageClassification, String scopeNamespace, String clientId) {
        setMessageClassification(messageClassification);
        setScope(scopeNamespace);
        setClientId(clientId);
    }

    @Override
    public String getMessageClassification() {
        return messageClassification;
    }

    @Override
    public void setMessageClassification(String messageClassification) {
        this.messageClassification = messageClassification;
    }

    @Override
    public String getScope() {
        return scopeNamespace;
    }

    @Override
    public void setScope(String scope) {
        this.scopeNamespace = scope;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the {@link KuraChannel} tokens in a user-friendly format.
     *
     * @return The {@link KuraChannel} tokens in a user-friendly format.
     * @since 1.2.0
     */
    @Override
    public String toString() {
        return Arrays.toString(getParts().toArray());
    }

    @Override
    public List<String> getParts() {
        return Lists.newArrayList(getMessageClassification(), getScope(), getClientId());
    }
}
