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
package org.eclipse.kapua.message;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link KapuaMessageFactory} definition.
 *
 * @since 1.0.0
 */
public interface KapuaMessageFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link KapuaMessage}
     *
     * @return The newly instantiated {@link KapuaMessage}
     * @since 1.0.0
     */
    KapuaMessage newMessage();

    /**
     * Instantiates a new {@link KapuaChannel}
     *
     * @return The newly instantiated {@link KapuaChannel}
     * @since 1.0.0
     */
    KapuaChannel newChannel();

    /**
     * Instantiates a new {@link KapuaPayload}
     *
     * @return The newly instantiated {@link KapuaPayload}
     * @since 1.0.0
     */
    KapuaPayload newPayload();

    /**
     * Instantiates a new {@link KapuaPosition}
     *
     * @return The newly instantiated {@link KapuaPosition}
     * @since 1.0.0
     */
    KapuaPosition newPosition();
}
