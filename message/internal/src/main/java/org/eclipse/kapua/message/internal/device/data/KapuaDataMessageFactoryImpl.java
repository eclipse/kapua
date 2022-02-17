/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.internal.device.data;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;

/**
 * {@link KapuaDataMessageFactory} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class KapuaDataMessageFactoryImpl implements KapuaDataMessageFactory {

    @Override
    public KapuaDataMessage newKapuaDataMessage() {
        return new KapuaDataMessageImpl();
    }

    @Override
    public KapuaDataChannel newKapuaDataChannel() {
        return new KapuaDataChannelImpl();
    }

    @Override
    public KapuaDataPayload newKapuaDataPayload() {
        return new KapuaDataPayloadImpl();
    }

}
