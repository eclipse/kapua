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
package org.eclipse.kapua.service.device.call.message.kura.data;

import org.eclipse.kapua.service.device.call.message.data.DeviceDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceDataChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraDataChannel extends KuraChannel implements DeviceDataChannel {

    private List<String> semanticParts;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraDataChannel() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param scopeNamespace The scope namespace
     * @param clientId       The clientId
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 1.0.0
     */
    public KuraDataChannel(String scopeNamespace, String clientId) {
        super(scopeNamespace, clientId);
    }

    @Override
    public List<String> getSemanticParts() {
        if (semanticParts == null) {
            semanticParts = new ArrayList<>();
        }

        return semanticParts;
    }

    @Override
    public void setSemanticParts(List<String> semanticParts) {
        this.semanticParts = semanticParts;
    }

    @Override
    public List<String> getParts() {
        List<String> parts = super.getParts();
        parts.addAll(getSemanticParts());
        return parts;
    }
}
