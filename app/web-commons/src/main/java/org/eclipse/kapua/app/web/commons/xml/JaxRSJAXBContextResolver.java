/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.web.commons.xml;

import org.eclipse.kapua.commons.util.xml.FallbackMappingJAXBContextProvider;

import javax.validation.constraints.NotNull;
import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;

/**
 * JaxRS-based applications {@link ContextResolver} implementation.
 * <p>
 * It relies on the {@link FallbackMappingJAXBContextProvider} implementation.
 *
 * @since 2.0.0
 */
public abstract class JaxRSJAXBContextResolver extends FallbackMappingJAXBContextProvider implements ContextResolver<JAXBContext> {

    @Override
    public JAXBContext getContext(@NotNull Class<?> clazz) {
        return getJAXBContext(clazz);
    }
}
