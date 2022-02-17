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
package org.eclipse.kapua.service.device.call.message.kura.app.request;

import org.eclipse.kapua.service.device.call.DeviceMethod;
import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppChannel;

import java.util.Arrays;
import java.util.List;

/**
 * {@link DeviceRequestChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraRequestChannel extends KuraAppChannel implements DeviceRequestChannel {

    /**
     * The {@link KuraMethod}.
     *
     * @since 1.0.0
     */
    private KuraMethod method;

    /**
     * The requested resources.
     *
     * @since 1.0.0
     */
    private String[] resources;

    /**
     * The request identifier.
     *
     * @since 1.0.0
     */
    private String requestId;

    /**
     * The requester identified.
     *
     * @since 1.0.0
     */
    private String requesterClientId;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraRequestChannel() {
        super();
    }

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 1.0.0
     */
    public KuraRequestChannel(String messageClassification, String scopeNamespace, String clientId) {
        super(messageClassification, scopeNamespace, clientId);
    }

    @Override
    public KuraMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(DeviceMethod method) {
        this.method = (KuraMethod) method;
    }

    @Override
    public String[] getResources() {
        if (resources == null) {
            resources = new String[0];
        }

        return resources;
    }

    @Override
    public void setResources(String[] resources) {
        this.resources = resources;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getRequesterClientId() {
        return requesterClientId;
    }

    @Override
    public void setRequesterClientId(String requesterClientId) {
        this.requesterClientId = requesterClientId;
    }

    @Override
    public List<String> getParts() {
        List<String> parts = super.getParts();
        parts.addAll(Arrays.asList(getResources()));
        return parts;
    }
}
