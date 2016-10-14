/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.data;

import java.util.List;

import org.eclipse.kapua.service.device.call.message.data.DeviceDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;

public class KuraDataChannel extends KuraChannel implements DeviceDataChannel
{

    private List<String> semanticChannelParts;

    @Override
    public List<String> getSemanticChannelParts()
    {
        return semanticChannelParts;
    }

    public void setSemanticChannelParts(List<String> semanticChannelParts)
    {
        this.semanticChannelParts = semanticChannelParts;
    }
}
