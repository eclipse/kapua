/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
