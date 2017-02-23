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
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;

@KapuaProvider
public class KapuaMessageFactoryImpl implements KapuaMessageFactory
{

    @Override
    public KapuaMessage<?,?> newMessage()
    {
        return new KapuaMessageImpl<KapuaChannel,KapuaPayload>();
    }

    @Override
    public KapuaChannel newChannel()
    {
        return new KapuaChannelImpl();
    }

    @Override
    public KapuaPayload newPayload()
    {
        return new KapuaPayloadImpl();
    }

    @Override

    public KapuaPosition newPosition()
    {
        return new KapuaPositionImpl();
    }

}
