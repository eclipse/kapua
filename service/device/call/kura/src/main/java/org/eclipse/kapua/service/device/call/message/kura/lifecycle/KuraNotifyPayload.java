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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.commons.util.Payloads;
import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

/**
 * Kura device notification message payload implementation.
 */
public class KuraNotifyPayload extends KuraPayload implements DevicePayload {

    /**
     * Returns a string for displaying
     *
     * @return A string used for displaying, never returns {@code null}
     */
    public String toDisplayString() {
        return Payloads.toDisplayString(metrics);
    }
}
