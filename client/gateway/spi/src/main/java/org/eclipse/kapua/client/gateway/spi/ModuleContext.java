/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.spi;

import java.util.Optional;

import org.eclipse.kapua.client.gateway.Client;

public interface ModuleContext {

    /**
     * Get the client instance this module is registered to
     * 
     * @return the client instance, never returns {@code null}
     */
    public Client getClient();

    /**
     * Adapt the module context to the requested class
     * 
     * @param clazz
     *            the class to adapt to
     * @return the result, never returns {@code null}, but may return {@link Optional#empty()}
     */
    public <T> Optional<T> adapt(Class<T> clazz);
}
