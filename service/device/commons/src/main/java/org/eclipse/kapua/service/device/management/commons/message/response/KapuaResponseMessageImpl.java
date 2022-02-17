/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.message.response;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

/**
 * {@link KapuaResponseMessage} implementation.
 *
 * @param <C> The {@link KapuaResponseChannel} type.
 * @param <P> The {@link KapuaResponsePayload} type.
 * @since 1.0.0
 */
public class KapuaResponseMessageImpl<C extends KapuaResponseChannel, P extends KapuaResponsePayload> extends KapuaMessageImpl<C, P>
        implements KapuaResponseMessage<C, P> {

    private static final long serialVersionUID = 8647251974722103216L;

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
