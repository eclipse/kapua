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
package org.eclipse.kapua.translator.jms.kura;

import com.google.inject.multibindings.Multibinder;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.jms.kura.data.TranslatorDataJmsKura;
import org.eclipse.kapua.translator.jms.kura.event.TranslatorEventConfigurationManagementJmsKura;
import org.eclipse.kapua.translator.jms.kura.lifecycle.TranslatorLifeAppsJmsKura;
import org.eclipse.kapua.translator.jms.kura.lifecycle.TranslatorLifeBirthJmsKura;
import org.eclipse.kapua.translator.jms.kura.lifecycle.TranslatorLifeDisconnectJmsKura;
import org.eclipse.kapua.translator.jms.kura.lifecycle.TranslatorLifeMissingJmsKura;
import org.eclipse.kapua.translator.jms.kura.notify.TranslatorLifeNotifyJmsKura;
import org.eclipse.kapua.translator.kura.jms.data.TranslatorDataKuraJms;

public class JmsKuraTranslatorsModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        final Multibinder<Translator> translatorMultibinder = Multibinder.newSetBinder(binder(), Translator.class);
        //org.eclipse.kapua.translator.jms.kura.data
        translatorMultibinder.addBinding().to(TranslatorDataJmsKura.class);
        //org.eclipse.kapua.translator.jms.kura.event
        translatorMultibinder.addBinding().to(TranslatorEventConfigurationManagementJmsKura.class);
        //org.eclipse.kapua.translator.jms.kura.lifecycle
        translatorMultibinder.addBinding().to(TranslatorLifeAppsJmsKura.class);
        translatorMultibinder.addBinding().to(TranslatorLifeBirthJmsKura.class);
        translatorMultibinder.addBinding().to(TranslatorLifeDisconnectJmsKura.class);
        translatorMultibinder.addBinding().to(TranslatorLifeMissingJmsKura.class);
        //org.eclipse.kapua.translator.jms.kura.notify
        translatorMultibinder.addBinding().to(TranslatorLifeNotifyJmsKura.class);
        //org.eclipse.kapua.translator.kura.jms.data
        translatorMultibinder.addBinding().to(TranslatorDataKuraJms.class);
    }
}
