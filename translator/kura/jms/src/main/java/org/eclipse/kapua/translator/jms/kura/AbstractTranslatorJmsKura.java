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
import org.eclipse.kapua.service.device.call.message.DeviceChannel;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

import java.util.Date;

/**
 * {@link Translator} {@code abstract} base implementation from {@link JmsMessage} to {@link DeviceMessage}.
 *
 * @param <C> The {@link DeviceChannel} type.
 * @param <P> The {@link DevicePayload} type.
 * @param <M> The {@link DeviceMessage} type.
 * @since 2.0.0
 */
public abstract class AbstractTranslatorJmsKura<C extends DeviceChannel, P extends DevicePayload, M extends DeviceMessage<C, P>>
        extends Translator<JmsMessage, M> {

    /**
     * The {@link DeviceMessage} type.
     *
     * @since 2.0.0
     */
    private final Class<M> messageClazz;

    /**
     * Constructor.
     * <p>
     * Defines the {@link DeviceMessage} type.
     *
     * @param messageClazz The specific {@link DeviceMessage} type.
     * @since 2.0.0
     */
    public AbstractTranslatorJmsKura(Class<M> messageClazz) {
        this.messageClazz = messageClazz;
    }

    @Override
    public M translate(JmsMessage jmsMessage) throws TranslateException {
        try {
            return createMessage(translate(jmsMessage.getTopic()),
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
     * @return The translated {@link DeviceChannel}
     * @throws InvalidChannelException in case that @link JmsTopic#getSplittedTopic()} has less than 3 tokens.
     * @since 2.0.0
     */
    public abstract C translate(JmsTopic jmsTopic) throws InvalidChannelException;

    /**
     * Translates the given {@link JmsPayload}
     *
     * @param jmsPayload the {@link JmsPayload} to translate.
     * @return The translated {@link DevicePayload}
     * @throws InvalidPayloadException in case that {@link JmsPayload#getBody()} is not {@link Kura} protobuf encoded and {@link #isRawPayloadToBody()} is {@code false}.
     * @since 1.2.0
     */
    public P translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            P kuraPayload = createPayload();

            if (jmsPayload.hasBody()) {

                try {
                    kuraPayload.readFromByteArray(jmsPayload.getBody());
                } catch (MessageException me) {
                    if (isRawPayloadToBody()) {
                        kuraPayload.setBody(jmsPayload.getBody());
                    } else {
                        throw me;
                    }
                }
            }

            return kuraPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, jmsPayload);
        }
    }

    /**
     * Whether a non-protobuf encoded {@link JmsPayload#getBody()} must be set as {@link KuraPayload#getBody()}.
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
     * Instantiates the specific {@link DeviceMessage} with the given parameters.
     *
     * @param deviceChannel The {@link DeviceChannel}.
     * @param receivedOn    The timestamp of the {@link JmsMessage#getReceivedOn()}
     * @param devicePayload The {@link DevicePayload}.
     * @return The newly created {@link DeviceMessage}
     * @since 2.0.0
     */
    public abstract M createMessage(C deviceChannel, Date receivedOn, P devicePayload);

    /**
     * Instantiates the specific {@link DeviceChannel} with the given parameters.
     *
     * @param messageClassifier The message classification.
     * @param scopeName         The scope namespace.
     * @param clientId          The clientId.
     * @return The newly instantiated {@link DeviceChannel}.
     * @since 2.0.0
     */
    public abstract C createChannel(String messageClassifier, String scopeName, String clientId);

    /**
     * Instantiates the specific {@link DevicePayload}.
     *
     * @return The newly instantiated {@link DevicePayload}.
     * @since 1.2.0
     */
    public abstract P createPayload();

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<M> getClassTo() {
        return messageClazz;
    }
}


