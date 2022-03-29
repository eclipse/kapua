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
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.commons.util.xml.DefaultJAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.xml.XmlAdaptedMetric;
import org.eclipse.kapua.message.xml.XmlAdaptedMetrics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Message Tests {@link JAXBContextProvider} implementation.
 * <p>
 * It relies {@link DefaultJAXBContextProvider} implementation.
 *
 * @since 1.0.0
 */
public class MessageTestJAXBContextProvider extends DefaultJAXBContextProvider implements JAXBContextProvider {

    private static final List<Class<?>> CONTEXT_CLASSES = Arrays.asList(
            KapuaMessage.class,
            KapuaChannel.class,
            KapuaPayload.class,
            KapuaPosition.class,

            XmlAdaptedMetric.class,
            XmlAdaptedMetrics.class
    );

    @Override
    protected List<Class<?>> getClasses() {
        return CONTEXT_CLASSES;
    }

    @Override
    protected Map<String, Object> getJaxbContextProperties() {
        return Collections.emptyMap();
    }
}
