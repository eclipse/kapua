/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.snapshot.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

/**
 * Device snapshot request message.
 */
public class SnapshotRequestMessage extends KapuaMessageImpl<SnapshotRequestChannel, SnapshotRequestPayload> implements KapuaRequestMessage<SnapshotRequestChannel, SnapshotRequestPayload> {

    private static final long serialVersionUID = 1L;

    @Override
    public Class<SnapshotRequestMessage> getRequestClass() {
        return SnapshotRequestMessage.class;
    }

    @Override
    public Class<SnapshotResponseMessage> getResponseClass() {
        return SnapshotResponseMessage.class;
    }

}
