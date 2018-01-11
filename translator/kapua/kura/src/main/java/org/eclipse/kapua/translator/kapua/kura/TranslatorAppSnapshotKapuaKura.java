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
package org.eclipse.kapua.translator.kapua.kura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.kura.app.SnapshotMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.configuration.snapshot.internal.SnapshotRequestChannel;
import org.eclipse.kapua.service.device.management.configuration.snapshot.internal.SnapshotRequestMessage;
import org.eclipse.kapua.service.device.management.configuration.snapshot.internal.SnapshotRequestPayload;
import org.eclipse.kapua.service.device.management.snapshot.internal.DeviceSnapshotAppProperties;

/**
 * Messages translator implementation from {@link SnapshotRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppSnapshotKapuaKura extends AbstractTranslatorKapuaKura<SnapshotRequestChannel, SnapshotRequestPayload, SnapshotRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<DeviceSnapshotAppProperties, SnapshotMetrics> PROPERTIES_DICTIONARY = new HashMap<>();

    static {
        PROPERTIES_DICTIONARY.put(DeviceSnapshotAppProperties.APP_NAME, SnapshotMetrics.APP_ID);
        PROPERTIES_DICTIONARY.put(DeviceSnapshotAppProperties.APP_VERSION, SnapshotMetrics.APP_VERSION);
    }

    protected KuraRequestChannel translateChannel(SnapshotRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(PROPERTIES_DICTIONARY.get(DeviceSnapshotAppProperties.APP_NAME).getValue())
                .append("-")
                .append(PROPERTIES_DICTIONARY.get(DeviceSnapshotAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());

        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));

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
        kuraRequestChannel.setResources(resources.toArray(new String[resources.size()]));

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    protected KuraRequestPayload translatePayload(SnapshotRequestPayload kapuaPayload) throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        if (kapuaPayload.getBody() != null) {
            kuraRequestPayload.setBody(kapuaPayload.getBody());
        }

        //
        // Return Kura Payload
        return kuraRequestPayload;
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
