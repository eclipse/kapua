/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
}
