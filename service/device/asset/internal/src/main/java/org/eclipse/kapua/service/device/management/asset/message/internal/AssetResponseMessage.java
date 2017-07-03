/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;

/**
 * Device bundle information response message.
 * 
 * @since 1.0
 * 
 */
public class AssetResponseMessage extends KapuaMessageImpl<AssetResponseChannel, AssetResponsePayload>
        implements KapuaResponseMessage<AssetResponseChannel, AssetResponsePayload> {

    private static final long serialVersionUID = 1L;
    private KapuaResponseCode responseCode;

    @Override
    public KapuaResponseCode getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(KapuaResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
