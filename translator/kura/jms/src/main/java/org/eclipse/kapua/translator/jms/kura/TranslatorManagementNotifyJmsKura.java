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
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.management.KuraNotifyChannel;
import org.eclipse.kapua.service.device.call.message.kura.management.KuraNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.management.KuraNotifyPayload;
import org.eclipse.kapua.service.device.call.message.kura.management.deploy.KuraDeployResources;
import org.eclipse.kapua.service.device.call.message.kura.management.deploy.KuraNotifyPackageDownloadPayload;
import org.eclipse.kapua.service.device.call.message.kura.management.deploy.KuraNotifyPackageInstallPayload;
import org.eclipse.kapua.service.device.call.message.kura.management.deploy.KuraNotifyPackageUninstallPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.transport.message.jms.JmsMessage} to {@link KuraNotifyMessage}
 *
 * @since 1.0
 */
public class TranslatorManagementNotifyJmsKura extends Translator<JmsMessage, KuraNotifyMessage> {

    @Override
    public KuraNotifyMessage translate(JmsMessage jmsMessage)
            throws KapuaException {
        KuraNotifyChannel kuraNotifyChannel = translate(jmsMessage.getTopic());

        KuraNotifyPayload kuraNotifyPayload = translate(jmsMessage.getPayload().getBody(), kuraNotifyChannel.getResources());

        return new KuraNotifyMessage(kuraNotifyChannel, jmsMessage.getReceivedOn(), kuraNotifyPayload);
    }

    private KuraNotifyChannel translate(JmsTopic jmsTopic)
            throws KapuaException {
        String[] topicTokens = jmsTopic.getSplittedTopic();
        // we shouldn't never get a shorter topic here (because that means we have issues on camel routing)
        // TODO check exception type
        if (topicTokens == null || topicTokens.length < 6) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }

        return new KuraNotifyChannel(topicTokens);
    }

    private KuraNotifyPayload translate(byte[] jmsBody, String resourceType)
            throws KapuaException {

        KuraNotifyPayload kuraNotifyPayload;

        if (KuraDeployResources.DOWNLOAD.equals(resourceType)) {
            kuraNotifyPayload = new KuraNotifyPackageDownloadPayload();
        } else if (KuraDeployResources.INSTALL.equals(resourceType)) {
            kuraNotifyPayload = new KuraNotifyPackageInstallPayload();
        } else if (KuraDeployResources.UNINSTALL.equals(resourceType)) {
            kuraNotifyPayload = new KuraNotifyPackageUninstallPayload();
        } else {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }

        kuraNotifyPayload.readFromByteArray(jmsBody);
        return kuraNotifyPayload;
    }

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraNotifyMessage> getClassTo() {
        return KuraNotifyMessage.class;
    }
}
