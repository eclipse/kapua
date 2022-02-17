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

import org.eclipse.kapua.service.device.call.kura.model.bundle.BundleMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestPayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link BundleRequestMessage} to {@link KuraRequestMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppBundleKapuaKura extends AbstractTranslatorKapuaKura<BundleRequestChannel, BundleRequestPayload, BundleRequestMessage> {

    @Override
    protected KuraRequestChannel translateChannel(BundleRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
            KuraRequestChannel kuraRequestChannel = TranslatorKapuaKuraUtils.buildBaseRequestChannel(BundleMetrics.APP_ID, BundleMetrics.APP_VERSION, kapuaChannel.getMethod());

            // Build resources
            List<String> resources = new ArrayList<>();
            switch (kapuaChannel.getMethod()) {
                case READ:
                    resources.add("bundles");
                    break;
                case EXECUTE: {
                    if (kapuaChannel.isStart()) {
                        resources.add("start");
                    } else {
                        resources.add("stop");
                    }

                    String bundleId = kapuaChannel.getBundleId();
                    if (bundleId != null) {
                        resources.add(bundleId);
                    }
                }
                break;
                case CREATE:
                case DELETE:
                case OPTIONS:
                case WRITE:
                default:
                    break;
            }
            kuraRequestChannel.setResources(resources.toArray(new String[0]));

            // Return Kura Channel
            return kuraRequestChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kapuaChannel);
        }
    }

    @Override
    protected KuraRequestPayload translatePayload(BundleRequestPayload kapuaPayload) throws InvalidPayloadException {
        try {
            KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

            if (kapuaPayload.hasBody()) {
                kuraRequestPayload.setBody(kapuaPayload.getBody());
            }

            return kuraRequestPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kapuaPayload);
        }
    }

    @Override
    public Class<BundleRequestMessage> getClassFrom() {
        return BundleRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

}
