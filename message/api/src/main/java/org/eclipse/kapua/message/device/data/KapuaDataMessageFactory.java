/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.device.data;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link KapuaDataMessage} definition.
 *
 * @since 1.1.0
 */
public interface KapuaDataMessageFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link KapuaDataMessage}
     *
     * @return The newly instantiated {@link KapuaDataMessage}
     * @since 1.1.0
     */
    public KapuaDataMessage newKapuaDataMessage();

    /**
     * Instantiates a new {@link KapuaDataChannel}
     *
     * @return The newly instantiated {@link KapuaDataChannel}
     * @since 1.1.0
     */
    KapuaDataChannel newKapuaDataChannel();

    /**
     * Instantiates a new {@link KapuaDataPayload}.
     *
     * @return The newly instantiated {@link KapuaDataPayload}.
     * @since 1.1.0
     */
    KapuaDataPayload newKapuaDataPayload();
}
