/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.device.data;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link KapuaDataMessage}s {@link KapuaObjectFactory} definition.
 *
 * @since 1.1.0
 */
public interface KapuaDataMessageFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link KapuaDataMessage}
     *
     * @return the new {@link KapuaDataMessage}
     * @since 1.1.0
     */
    public KapuaDataMessage newKapuaDataMessage();

    /**
     * Creates a new {@link KapuaDataChannel}
     *
     * @return the new {@link KapuaDataChannel}
     * @since 1.1.0
     */
    KapuaDataChannel newKapuaDataChannel();

    /**
     * Creates a new {@link KapuaDataPayload}.
     *
     * @return the new {@link KapuaDataPayload}.
     * @since 1.1.0
     */
    KapuaDataPayload newKapuaDataPayload();
}
