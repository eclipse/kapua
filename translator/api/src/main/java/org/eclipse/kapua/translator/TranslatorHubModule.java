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

import com.google.inject.Singleton;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;

public class TranslatorHubModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        // Singleton: the injected Translators are shared between all callers and threads, so they must be stateless
        bind(TranslatorHub.class).to(TranslatorHubImpl.class).in(Singleton.class);
    }
}
