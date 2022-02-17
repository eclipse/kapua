/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.service.device.call.kura.model.snapshot.SnapshotMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestChannel;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestPayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link SnapshotRequestMessage} to {@link KuraRequestMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppSnapshotKapuaKura extends AbstractTranslatorKapuaKura<SnapshotRequestChannel, SnapshotRequestPayload, SnapshotRequestMessage> {

    @Override
    protected KuraRequestChannel translateChannel(SnapshotRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
            KuraRequestChannel kuraRequestChannel = TranslatorKapuaKuraUtils.buildBaseRequestChannel(SnapshotMetrics.APP_ID, SnapshotMetrics.APP_VERSION, kapuaChannel.getMethod());

            // Build resources
            List<String> resources = new ArrayList<>();
            switch (kapuaChannel.getMethod()) {
                case EXECUTE:
                    resources.add("rollback");
                    break;
                case READ:
                    resources.add("snapshots");
                    break;
                case CREATE:
                case DELETE:
                case OPTIONS:
                case WRITE:
                default:
                    break;
            }

            String snapshotId = kapuaChannel.getSnapshotId();
            if (snapshotId != null) {
                resources.add(snapshotId);
            }
            kuraRequestChannel.setResources(resources.toArray(new String[0]));

            // Return Kura Channel
            return kuraRequestChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kapuaChannel);
        }
    }

    @Override
    protected KuraRequestPayload translatePayload(SnapshotRequestPayload kapuaPayload) throws InvalidPayloadException {
        try {
            KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

            if (kapuaPayload.hasBody()) {
                kuraRequestPayload.setBody(kapuaPayload.getBody());
            }

            // Return Kura Payload
            return kuraRequestPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kapuaPayload);
        }
    }

    @Override
    public Class<SnapshotRequestMessage> getClassFrom() {
        return SnapshotRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }
}
