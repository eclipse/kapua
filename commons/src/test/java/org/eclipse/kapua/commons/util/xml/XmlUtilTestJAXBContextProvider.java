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
package org.eclipse.kapua.commons.util.xml;

import java.util.Collections;
import java.util.List;

/**
 * {@link JAXBContextProvider} implementation to be used in {@link XmlUtilTest}.
 *
 * @since 2.0.0
 */
public class XmlUtilTestJAXBContextProvider extends DefaultJAXBContextProvider implements JAXBContextProvider {

    private static final List<Class<?>> CLASSES_TO_BOUND = Collections.singletonList(
            XmlUtilTestObject.class
    );

    @Override
    public List<Class<?>> getClassesToBound() {
        return CLASSES_TO_BOUND;
    }

}
