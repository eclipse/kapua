/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.security.registration;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * A provider of {@link RegistrationProcessor}s
 * <p>
 * This class is intended to be used in combination with Java's {@link ServiceLoader} framework.
 * Providers can be registered with the service framework and then be iterated over, creating new
 * instances of {@link RegistrationProcessor}.
 * </p>
 * <p>
 * The reason from splitting this up in two classes is that the providers don't require a lifecycle
 * and can just be discarded, while the processors implement {@link AutoCloseable} and most be
 * properly closed. So the providers are considered simple and lightweight classes.
 * </p>
 */
public interface RegistrationProcessorProvider {

    /**
     * Create a collection of supported {@link RegistrationProcessor}s
     * <p>
     * <b>Note:</b> The caller takes ownership of the returned resources and must close them
     * properly once they are no longer used.
     * </p>
     *
     * @return A collection of processors provided by this provider
     */
    Collection<? extends RegistrationProcessor> createAll();
}
