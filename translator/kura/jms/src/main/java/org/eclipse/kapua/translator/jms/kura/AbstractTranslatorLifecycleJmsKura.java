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
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.AbstractKuraLifecycleChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.AbstractKuraLifecycleMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.AbstractKuraLifecyclePayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleChannel;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

import java.util.Date;

/**
 * {@link Translator} {@code abstract} base implementation from {@link JmsMessage} to {@link AbstractKuraLifecycleMessage}
 *
 * @param <C> The {@link AbstractKuraLifecycleChannel} type.
 * @param <P> The {@link AbstractKuraLifecyclePayload} type.
 * @param <M> The {@link AbstractKuraLifecycleMessage} type.
 * @since 1.2.0
 */
public abstract class AbstractTranslatorLifecycleJmsKura<C extends DeviceLifecycleChannel, P extends DeviceLifecyclePayload, M extends DeviceLifecycleMessage<C, P>> extends Translator<JmsMessage, M> {

    /**
     * The {@link AbstractKuraLifecycleMessage} type.
     *
     * @since 1.2.0
     */
    private final Class<M> lifecycleMessageClazz;

    /**
     * Constructor.
     * <p>
     * Defines the {@link AbstractKuraLifecycleMessage} type.
     *
     * @param lifecycleMessageClazz The specific {@link AbstractKuraLifecycleMessage} type.
     * @since 1.2.0
     */
    public AbstractTranslatorLifecycleJmsKura(Class<M> lifecycleMessageClazz) {
        this.lifecycleMessageClazz = lifecycleMessageClazz;
    }

    @Override
    public M translate(JmsMessage jmsMessage) throws TranslateException {
        try {
            return createLifecycleMessage(translate(jmsMessage.getTopic()),
                    jmsMessage.getReceivedOn(),
                    translate(jmsMessage.getPayload()));
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, jmsMessage);
        }
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
    public C translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();
            if (topicTokens.length < 3) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            return createLifecycleChannel(topicTokens[0], topicTokens[1], topicTokens[2]);
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    /**
     * Translates the given {@link JmsPayload}
     *
     * @param jmsPayload the {@link JmsPayload} to translate.
     * @return The translated {@link AbstractKuraLifecyclePayload}
     * @throws InvalidPayloadException in case that {@link JmsPayload#getBody()} is not {@link Kura} protobuf encoded and {@link #isRawPayloadToBody()} is {@code false}.
     * @since 1.2.0
     */
    public P translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            P kuraLifecyclePayload = createLifecyclePayload();

            if (jmsPayload.hasBody()) {

                try {
                    kuraLifecyclePayload.readFromByteArray(jmsPayload.getBody());
                } catch (MessageException me) {
                    if (isRawPayloadToBody()) {
                        kuraLifecyclePayload.setBody(jmsPayload.getBody());
                    } else {
                        throw me;
                    }
                }
            }

            return kuraLifecyclePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, jmsPayload);
        }
    }

    /**
     * Whether or not a non-protobuf encoded {@link JmsPayload#getBody()} must be set as {@link KuraPayload#getBody()}.
     * If not an exception is thrown.
     * Defaults to {@link Boolean#FALSE}.
     *
     * @return {@code true} if a non-protobuf encoded {@link JmsPayload#getBody()} must be set as {@link KuraPayload#getBody()}, {@code false} otherwise
     * @since 1.2.0
     */
    public boolean isRawPayloadToBody() {
        return Boolean.FALSE;
    }

    /**
     * Instantiates the specific {@link AbstractKuraLifecycleMessage} with the given parameters.
     *
     * @param lifecycleChannel The {@link AbstractKuraLifecycleChannel}.
     * @param receivedOn       The timestamp of the {@link JmsMessage#getReceivedOn()}
     * @param lifecyclePayload The AbstractKuraLifecyclePayload.
     * @return The newly created {@link AbstractKuraLifecycleMessage}
     * @since 1.2.0
     */
    public abstract M createLifecycleMessage(C lifecycleChannel, Date receivedOn, P lifecyclePayload);

    /**
     * Instantiates the specific {@link AbstractKuraLifecycleChannel} with the given parameters.
     *
     * @param messageClassifier The message classification.
     * @param scopeName         The scope namespace.
     * @param clientId          The clientId.
     * @return The newly instantiated {@link AbstractKuraLifecycleChannel}.
     * @since 1.2.0
     */
    public abstract C createLifecycleChannel(String messageClassifier, String scopeName, String clientId);

    /**
     * Instantiates the specific {@link AbstractKuraLifecyclePayload}.
     *
     * @return The newly instantiated {@link AbstractKuraLifecyclePayload}.
     * @since 1.2.0
     */
    public abstract P createLifecyclePayload();

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<M> getClassTo() {
        return lifecycleMessageClazz;
    }
}


