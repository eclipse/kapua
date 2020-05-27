/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

/**
 * Package request message.
 */
public class PackageRequestMessage extends KapuaMessageImpl<PackageRequestChannel, PackageRequestPayload>
        implements KapuaRequestMessage<PackageRequestChannel, PackageRequestPayload> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public Class<PackageRequestMessage> getRequestClass() {
        return PackageRequestMessage.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<PackageResponseMessage> getResponseClass() {
        return PackageResponseMessage.class;
    }

}
