/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.client.protocol;

import javax.inject.Singleton;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.client.setting.ServiceClientSetting;

public class ProtocolDescriptorModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(ServiceClientSetting.class).in(Singleton.class);
        bind(ProtocolDescriptorProvider.class).to(DefaultProtocolDescriptionProvider.class).in(Singleton.class);
    }
}
