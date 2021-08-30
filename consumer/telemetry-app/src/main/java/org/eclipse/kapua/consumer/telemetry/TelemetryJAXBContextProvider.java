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
package org.eclipse.kapua.consumer.telemetry;

import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreXmlRegistry;
import org.eclipse.kapua.commons.util.xml.FallbackMappingJAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * {@link TelemetryApplication} {@link JAXBContextProvider}.
 * <p>
 * It relies on the {@link FallbackMappingJAXBContextProvider} implementation.
 *
 * @since 2.0.0
 */
public class TelemetryJAXBContextProvider extends FallbackMappingJAXBContextProvider implements JAXBContextProvider {

    @Override
    protected List<Class<?>> getClassesToBound() {
        return Arrays.asList(
                // Kapua Service Configuration
                KapuaTmetadata.class,
                KapuaTocd.class,
                KapuaTad.class,
                KapuaTicon.class,
                TscalarImpl.class,
                KapuaToption.class,
                KapuaTdesignate.class,
                KapuaTobject.class,
                MetatypeXmlRegistry.class,

                // Kapua Event
                ServiceEvent.class,
                EventStoreRecordCreator.class,
                EventStoreRecordListResult.class,
                EventStoreRecordQuery.class,
                EventStoreXmlRegistry.class
        );
    }
}
