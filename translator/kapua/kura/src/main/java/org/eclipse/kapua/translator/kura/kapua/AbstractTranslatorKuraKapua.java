/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.translator.Translator;

public abstract class AbstractTranslatorKuraKapua<TO_C extends KapuaChannel, TO_P extends KapuaPayload, TO_M extends KapuaMessage<TO_C, TO_P>> extends Translator<KuraResponseMessage, TO_M> {

    @Override
    public TO_M translate(KuraResponseMessage kuraMessage) throws KapuaException {

        final KapuaLocator locator = KapuaLocator.getInstance();
        final AccountService accountService = locator.getService(AccountService.class);
        final Account account = accountService.findByName(kuraMessage.getChannel().getScope());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraMessage.getChannel().getScope());
        }

        return translateMessage(kuraMessage, account);
    }

    protected abstract TO_M translateMessage(KuraResponseMessage kuraMessage, Account account) throws KapuaException;

    protected abstract TO_C translateChannel(KuraResponseChannel kuraChannel) throws KapuaException;

    protected abstract TO_P translatePayload(KuraResponsePayload kuraPayload) throws KapuaException;

}
