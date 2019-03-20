/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedChannel;

/**
 * {@link KapuaUnmatchedChannel} implementation.
 *
 * @since 1.0.0
 */
public class KapuaUnmatchedChannelImpl extends AbstractLifecycleChannelImpl implements KapuaUnmatchedChannel {

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Client id '");
        strBuilder.append(getClientId());
        strBuilder.append("' - semantic topic '");
        strBuilder.append(super.toString());
        strBuilder.append("'");
        return strBuilder.toString();
    }

}
