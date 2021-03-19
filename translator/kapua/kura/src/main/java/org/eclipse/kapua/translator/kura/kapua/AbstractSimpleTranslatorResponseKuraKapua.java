/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponsePayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@code abstract} implementation for {@link KuraResponseMessage} to {@link KapuaResponseMessage}
 *
 * @since 1.0.0
 */
public abstract class AbstractSimpleTranslatorResponseKuraKapua<TO_C extends KapuaResponseChannel, TO_P extends KapuaResponsePayload, TO_M extends KapuaResponseMessage<TO_C, TO_P>>
        extends AbstractTranslatorResponseKuraKapua<TO_C, TO_P, TO_M> {

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);
    private static final boolean SHOW_STACKTRACE = DeviceManagementSetting.getInstance().getBoolean(DeviceManagementSettingKey.SHOW_STACKTRACE, false);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private final Class<TO_M> messageClazz;
    private final Class<TO_P> payloadClazz;

    /**
     * Constructor.
     *
     * @param messageClazz The {@link Class} of the {@link KapuaResponseMessage}. It must have a 0-arguments constructor.
     * @param payloadClazz The {@link Class} of the {@link KapuaResponsePayload}. It must have a 0-arguments constructor.
     * @since 1.0.0
     */
    public AbstractSimpleTranslatorResponseKuraKapua(Class<TO_M> messageClazz, Class<TO_P> payloadClazz) {
        this.messageClazz = messageClazz;
        this.payloadClazz = payloadClazz;
    }

    @Override
    protected TO_M createMessage() throws KapuaException {
        GenericRequestFactory genericRequestFactory = LOCATOR.getFactory(GenericRequestFactory.class);

        try {
            if (this.messageClazz.equals(GenericResponseMessage.class)) {
                return this.messageClazz.cast(genericRequestFactory.newResponseMessage());
            } else {
                return this.messageClazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public Class<KuraResponseMessage> getClassFrom() {
        return KuraResponseMessage.class;
    }

    @Override
    public Class<TO_M> getClassTo() {
        return this.messageClazz;
    }

    @Override
    protected abstract TO_C translateChannel(KuraResponseChannel kuraResponseChannel) throws InvalidChannelException;

    @Override
    protected TO_P translatePayload(KuraResponsePayload kuraResponsePayload) throws InvalidPayloadException {
        try {
            GenericRequestFactory genericRequestFactory = LOCATOR.getFactory(GenericRequestFactory.class);

            TO_P appResponsePayload;
            if (payloadClazz.equals(GenericResponsePayload.class)) {
                appResponsePayload = this.payloadClazz.cast(genericRequestFactory.newResponsePayload());
            } else {
                appResponsePayload = payloadClazz.newInstance();
            }

            appResponsePayload.setExceptionMessage(kuraResponsePayload.getExceptionMessage());

            if (SHOW_STACKTRACE) {
                appResponsePayload.setExceptionStack(kuraResponsePayload.getExceptionStack());
                kuraResponsePayload.getMetrics().remove(KuraResponseMetrics.EXCEPTION_STACK.getName());
            }

            return appResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraResponsePayload);
        }
    }

    /**
     * Reads the given {@code byte[]} as the requested {@code returnAs} parameter.
     *
     * @param bytesToRead The {@link KapuaResponsePayload#getBody()}
     * @param returnAs    The {@link Class} to read as.
     * @param <T>         The type of the retrun.
     * @return Returns the given {@code byte[]} as the given {@link Class}
     * @throws TranslatorException If the {@code byte[]} is not uspported or the {@code byte[]} cannot be read as the given {@link Class}
     * @since 1.5.0
     */
    protected <T> T readXmlBodyAs(byte[] bytesToRead, Class<T> returnAs) throws TranslatorException {
        String body;
        try {
            body = new String(bytesToRead, CHAR_ENCODING);
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, (Object) bytesToRead);
        }

        try {
            return XmlUtil.unmarshal(body, returnAs);
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, body);
        }
    }
}
