/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.misc;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;

import com.google.inject.Provides;

public class TestConfigModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(JAXBContextProvider.class).to(TestJAXBContextProvider.class).in(Singleton.class);
    }

    @Provides
    @Named("metricModuleName")
    String metricModuleName() {
        return "qa-tests";
    }

    @Provides
    @Named("eventsModuleName")
    String eventModuleName() {
        return "qa_tests";
    }
}
