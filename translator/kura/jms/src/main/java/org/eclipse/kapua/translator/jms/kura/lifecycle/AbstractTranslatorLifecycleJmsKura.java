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
package org.eclipse.kapua.translator.jms.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.lifecycle.AbstractKuraLifecycleChannel;
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
public abstract class AbstractTranslatorLifecycleJmsKura<C extends DeviceLifecycleChannel, P extends DeviceLifecyclePayload, M extends DeviceLifecycleMessage<C, P>> extends AbstractTranslatorJmsKura<C, P, M> {

    /**
     * Constructor.
     * <p>
     * Defines the {@link DeviceLifecycleMessage} type.
     *
     * @param lifecycleMessageClazz The specific {@link DeviceLifecycleMessage} type.
     * @since 1.2.0
     */
    public AbstractTranslatorLifecycleJmsKura(Class<M> lifecycleMessageClazz) {
        super(lifecycleMessageClazz);
    }

    /**
     * Translates the given {@link JmsTopic}.
     * <p>
     * Checks that the {@link JmsTopic#getSplittedTopic()} has at least 3 tokens.
     *
     * @param jmsTopic The {@link JmsTopic} to translate
     * @return The translated {@link AbstractKuraLifecycleChannel}
     * @throws InvalidChannelException in case that @link JmsTopic#getSplittedTopic()} has less than 3 tokens.
     * @since 1.2.0
     */
    @Override
    public C translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();
            // $EDC/{account}/{clientId}/MQTT/...
            if (topicTokens.length < 3) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            return createChannel(topicTokens[0], topicTokens[1], topicTokens[2]);
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    /**
     * Instantiates the specific {@link DeviceLifecycleChannel} with the given parameters.
     *
     * @param messageClassifier The message classification.
     * @param scopeName         The scope namespace.
     * @param clientId          The clientId.
     * @return The newly instantiated {@link DeviceLifecycleChannel}.
     * @since 1.2.0
     */
    @Override
    public abstract C createChannel(String messageClassifier, String scopeName, String clientId);
}


