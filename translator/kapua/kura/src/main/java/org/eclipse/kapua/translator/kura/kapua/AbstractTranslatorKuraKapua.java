/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link Translator} {@code abstract} implementation from {@link KuraResponseMessage} to {@link KapuaMessage}
 *
 * @since 1.0.0
 */
public abstract class AbstractTranslatorKuraKapua<TO_C extends KapuaChannel, TO_P extends KapuaPayload, TO_M extends KapuaMessage<TO_C, TO_P>> extends Translator<KuraResponseMessage, TO_M> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);

    @Override
    public TO_M translate(KuraResponseMessage kuraMessage) throws TranslateException {
        try {
            Account account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.findByName(kuraMessage.getChannel().getScope()));

            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, kuraMessage.getChannel().getScope());
            }

            return translateMessage(kuraMessage, account);
        } catch (TranslateException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraMessage);
        }
    }

    /**
     * @since 1.0.0
     */
    protected abstract TO_M translateMessage(KuraResponseMessage kuraMessage, Account account) throws TranslateException;

    /**
     * @since 1.0.0
     */
    protected abstract TO_C translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException;

    /**
     * @since 1.0.0
     */
    protected abstract TO_P translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException;

}
