/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;

public class DeviceMatchPredicate<T> extends AbstractMatchPredicate<T> {

    /**
     * Constructor.
     * <p>
     *
     * @param matchTerm
     * @since 1.2.0
     */
    public DeviceMatchPredicate(T matchTerm) {
        this.attributeNames = new ArrayList<>(Arrays.asList(
                DeviceAttributes.CLIENT_ID,
                DeviceAttributes.DISPLAY_NAME,
                DeviceAttributes.SERIAL_NUMBER,
                DeviceAttributes.MODEL_ID,
                DeviceAttributes.MODEL_NAME,
                DeviceAttributes.BIOS_VERSION,
                DeviceAttributes.FIRMWARE_VERSION,
                DeviceAttributes.OS_VERSION,
                DeviceAttributes.JVM_VERSION,
                DeviceAttributes.OSGI_FRAMEWORK_VERSION,
                DeviceAttributes.APPLICATION_FRAMEWORK_VERSION,
                DeviceAttributes.CONNECTION_INTERFACE,
                DeviceAttributes.CONNECTION_IP)
        );
        this.matchTerm = matchTerm;
    }

}
