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
package org.eclipse.kapua.locator;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@link KapuaService} loader definition.
 *
 * @since 1.0.0
 */
public interface KapuaServiceLoader {

    /**
     * Returns an implementing instance the requested {@link KapuaService}.
     *
     * @param serviceClass The {@link KapuaService} class to retrieve.
     * @return The requested {@link KapuaService} implementation.
     * @throws KapuaRuntimeException with KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE code if service is not available
     * @since 1.0.0
     */
    <S extends KapuaService> S getService(@NotNull Class<S> serviceClass);

    /**
     * Returns an implementing instance the requested {@link KapuaObjectFactory}.
     *
     * @param factoryClass The {@link KapuaObjectFactory}to retrieve.
     * @return The requested {@link KapuaObjectFactory} implementation.
     * @since 1.0.0
     */
    <F extends KapuaObjectFactory> F getFactory(@NotNull Class<F> factoryClass);

    /**
     * Returns the {@link List} of all the available {@link KapuaService} implementations.
     *
     * @return The {@link List} of all the available {@link KapuaService} implementations.
     * @since 1.0.0
     */
    List<KapuaService> getServices();
}
