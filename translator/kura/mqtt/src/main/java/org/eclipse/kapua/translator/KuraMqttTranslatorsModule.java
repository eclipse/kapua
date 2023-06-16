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
package org.eclipse.kapua.translator;

import com.google.inject.multibindings.Multibinder;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.translator.kura.mqtt.TranslatorDataKuraMqtt;
import org.eclipse.kapua.translator.kura.mqtt.TranslatorRequestKuraMqtt;
import org.eclipse.kapua.translator.mqtt.kura.TranslatorDataMqttKura;
import org.eclipse.kapua.translator.mqtt.kura.TranslatorResponseMqttKura;

public class KuraMqttTranslatorsModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        final Multibinder<Translator> translatorMultibinder = Multibinder.newSetBinder(binder(), Translator.class);
        //org.eclipse.kapua.translator.kura.mqtt
        translatorMultibinder.addBinding().to(TranslatorDataKuraMqtt.class);
        translatorMultibinder.addBinding().to(TranslatorRequestKuraMqtt.class);
        //org.eclipse.kapua.translator.mqtt.kura
        translatorMultibinder.addBinding().to(TranslatorDataMqttKura.class);
        translatorMultibinder.addBinding().to(TranslatorResponseMqttKura.class);
    }
}
