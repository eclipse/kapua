/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura.event;

import org.eclipse.kapua.service.device.call.message.app.event.DeviceManagementEventChannel;
import org.eclipse.kapua.service.device.call.message.app.event.DeviceManagementEventMessage;
import org.eclipse.kapua.service.device.call.message.app.event.DeviceManagementEventPayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleChannel;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.translator.jms.kura.AbstractTranslatorJmsKura;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * {@link Translator} {@code abstract} base implementation from {@link JmsMessage} to {@link DeviceLifecycleMessage}
 *
 * @param <C> The {@link DeviceLifecycleChannel} type.
 * @param <P> The {@link DeviceLifecyclePayload} type.
 * @param <M> The {@link DeviceLifecycleMessage} type.
 * @since 1.2.0
 */
public abstract class AbstractTranslatorEventJmsKura<C extends DeviceManagementEventChannel, P extends DeviceManagementEventPayload, M extends DeviceManagementEventMessage<C, P>>
        extends AbstractTranslatorJmsKura<C, P, M> {

    /**
     * Constructor.
     * <p>
     * Defines the {@link DeviceManagementEventMessage} type.
     *
     * @param deviceManagementEventClazz The specific {@link DeviceManagementEventMessage} type.
     * @since 2.0.0
     */
    public AbstractTranslatorEventJmsKura(Class<M> deviceManagementEventClazz) {
        super(deviceManagementEventClazz);
    }

    /**
     * Translates the given {@link JmsTopic}.
     * <p>
     * Checks that the {@link JmsTopic#getSplittedTopic()} has at least 3 tokens.
     *
     * @param jmsTopic The {@link JmsTopic} to translate
     * @return The translated {@link DeviceManagementEventChannel}
     * @throws InvalidChannelException in case that @link JmsTopic#getSplittedTopic()} has less than 3 tokens.
     * @since 1.2.0
     */
    @Override
    public C translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();

            // $EDC/{account}/{clientId}/{appName}/(appVersion}/...
            if (topicTokens.length < 5) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            C deviceManagementEventChannel = createChannel(topicTokens[0], topicTokens[1], topicTokens[2]);
            deviceManagementEventChannel.setAppName(topicTokens[3]);
            deviceManagementEventChannel.setAppVersion(topicTokens[4]);

            return deviceManagementEventChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    /**
     * Instantiates the specific {@link DeviceManagementEventChannel} with the given parameters.
     *
     * @param messageClassifier The message classification.
     * @param scopeName         The scope namespace.
     * @param clientId          The clientId.
     * @return The newly instantiated {@link DeviceManagementEventChannel}.
     * @since 2.0.0
     */
    @Override
    public abstract C createChannel(String messageClassifier, String scopeName, String clientId);
}


