/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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
 * {@link KapuaMessage}s {@link KapuaObjectFactory} definition.
 *
 * @since 1.0.0
 */
public interface KapuaMessageFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link KapuaMessage}
     *
     * @return the new {@link KapuaMessage}
     * @since 1.0.0
     */
    KapuaMessage newMessage();

    /**
     * Creates a new {@link KapuaChannel}
     *
     * @return the new {@link KapuaChannel}
     * @since 1.0.0
     */
    KapuaChannel newChannel();

    /**
     * Creates a new {@link KapuaPayload}
     *
     * @return the new {@link KapuaPayload}
     * @since 1.0.0
     */
    KapuaPayload newPayload();

    /**
     * Creates a new {@link KapuaPosition}
     *
     * @return the new {@link KapuaPosition}
     * @since 1.0.0
     */
    KapuaPosition newPosition();
}
