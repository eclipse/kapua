/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.translator.test.steps;

import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestMessage;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.TranslateException;

public class ExampleTranslator extends Translator {
    @Override
    public Message translate(Message message) throws TranslateException {
        return message;
    }

    @Override
    public Class<BundleRequestMessage> getClassFrom() {
        return BundleRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

    public Class<? extends Message> getClass(Class<? extends Message> returnedClass) {
        return returnedClass;
    }
}